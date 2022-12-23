package yoonleeverse.onlinejudge.api.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.common.dto.EmailMessage;
import yoonleeverse.onlinejudge.api.common.service.EmailService;
import yoonleeverse.onlinejudge.api.user.UserComponent;
import yoonleeverse.onlinejudge.api.user.dto.*;
import yoonleeverse.onlinejudge.api.user.entity.OAuthLink;
import yoonleeverse.onlinejudge.api.user.entity.ResetCodeStorage;
import yoonleeverse.onlinejudge.api.user.entity.TokenStorage;
import yoonleeverse.onlinejudge.api.user.entity.UserEntity;
import yoonleeverse.onlinejudge.api.user.repository.OAuthLinkRedisRepository;
import yoonleeverse.onlinejudge.api.user.repository.ResetCodeStorageRedisRepository;
import yoonleeverse.onlinejudge.api.user.repository.TokenStorageRedisRepository;
import yoonleeverse.onlinejudge.api.user.repository.UserRepository;
import yoonleeverse.onlinejudge.config.AppProperties;
import yoonleeverse.onlinejudge.security.UserPrincipal;
import yoonleeverse.onlinejudge.util.CookieUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import static yoonleeverse.onlinejudge.api.common.constant.Constants.Cookie.REFRESH_TOKEN;
import static yoonleeverse.onlinejudge.api.common.constant.Constants.ERole.ROLE_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuthLinkRedisRepository oAuthLinkRedisRepository;
    private final TokenStorageRedisRepository tokenStorageRedisRepository;
    private final UserComponent userComponent;
    private final EmailService emailService;
    private final AppProperties appProperties;
    private final ResetCodeStorageRedisRepository resetCodeStorageRedisRepository;

    @Override
    public SignUpResponse signUp(HttpServletResponse response, SignUpRequest req) {

        String email = req.getEmail();
        String password = req.getPassword();
        String name = req.getName();
        String linkKey = req.getLinkKey();
        boolean isOAuth = StringUtils.isNotEmpty(linkKey);

        if (!isOAuth) {
            validateEmail(email);
        }

        if (!userComponent.checkName(name)) {
            throw new RuntimeException("사용할 수 없는 닉네임입니다.");
        }

        UserEntity user = UserEntity.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(password))
                .roles(new HashSet<>())
                .links(new ArrayList<>())
                .enabled(true)
                .build();

        if (isOAuth) {
            OAuthLink oAuthLink = oAuthLinkRedisRepository.findById(linkKey)
                    .orElseThrow(() -> new RuntimeException("OAuth 인증 정보가 존재하지 않습니다."));

            user.addOAuthLink(OAuthLink.of(oAuthLink));
            user.addRole(ROLE_USER);
            oAuthLinkRedisRepository.delete(oAuthLink);
        } else {
            String verifyCode = UUID.randomUUID().toString();
            user.setVerifyCode(verifyCode);
            String verifyUrl = String.format(appProperties.getVerifyUrl(), verifyCode);
            emailService.sendMessage(EmailMessage.of(email, "[UNI Online Judge] 이메일 인증", verifyUrl));
        }

        userRepository.save(user);

        String accessToken = userComponent.issueToken(response, email);

        return SignUpResponse.ofSuccess(accessToken);
    }

    @Override
    public CurrentUserResponse getCurrentUser(String id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("존재하지 않는 회원입니다."));
        return CurrentUserResponse.ofSuccess(user);
    }

    @Override
    public SignInResponse signIn(HttpServletResponse response, SignInRequest req) {

        String email = req.getEmail();
        String password = req.getPassword();
        String linkKey = req.getLinkKey();
        boolean isOAuth = StringUtils.isNotEmpty(linkKey);

        UserEntity user;

        if (isOAuth) {
            OAuthLink oAuthLink = oAuthLinkRedisRepository.findById(linkKey)
                    .orElseThrow(() -> new RuntimeException("OAuth 인증 정보가 존재하지 않습니다."));

            user = userRepository.findByProviderAndUserId(oAuthLink.getProvider(), oAuthLink.getUserId())
                    .orElseThrow(() -> new BadCredentialsException("존재하지 않는 회원입니다."));
        } else {
            user = userRepository.findById(email)
                    .orElseThrow(() -> new BadCredentialsException("존재하지 않는 회원입니다."));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("비밀번호가 틀렸습니다.");
            }
        }

        if (!user.isEnabled()) {
            throw new BadCredentialsException("탈퇴한 회원입니다.");
        }

        String accessToken = userComponent.issueToken(response, user.getEmail());

        return SignInResponse.ofSuccess(accessToken, user);
    }

    @Override
    public APIResponse checkName(String name) {

        APIResponse response = new APIResponse();
        response.setSuccess(userComponent.checkName(name));

        return response;
    }

    @Override
    public APIResponse signOut(HttpServletRequest request, HttpServletResponse response, UserPrincipal userPrincipal) {

        if (userPrincipal != null) {
            tokenStorageRedisRepository.deleteByUserId(userPrincipal.getUsername());
        }

        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite(SameSite.NONE.name())
                .secure(true)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new APIResponse();
    }

    @Override
    public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        Cookie cookie = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .orElse(null);
        log.debug("refreshToken cookie[{}]", cookie);
        if (cookie == null) {
            return RefreshTokenResponse.ofFail();
        }

        String oldRefreshToken = cookie.getValue();
        TokenStorage oldTokenStorage = tokenStorageRedisRepository.findById(oldRefreshToken)
                .orElse(null);
        log.debug("refreshToken oldTokenStorage[{}]", oldTokenStorage);
        if (oldTokenStorage == null) {
            return RefreshTokenResponse.ofFail();
        }

        String id = oldTokenStorage.getUserId();
        log.debug("refreshToken id[{}]", id);
        tokenStorageRedisRepository.delete(oldTokenStorage);
        String accessToken = userComponent.issueToken(response, id);

        return RefreshTokenResponse.ofSuccess(accessToken);
    }

    @Override
    public APIResponse addSnsAccount(String linkKey, String username) {

        UserEntity user = userRepository.findById(username)
                .orElseThrow(() -> new BadCredentialsException("존재하지 않는 회원입니다."));

        if (StringUtils.isNotEmpty(linkKey)) {
            OAuthLink oAuthLink = oAuthLinkRedisRepository.findById(linkKey)
                    .orElseThrow(() -> new RuntimeException("OAuth 인증 정보가 존재하지 않습니다."));

            user.addOAuthLink(OAuthLink.of(oAuthLink));
            oAuthLinkRedisRepository.delete(oAuthLink);
        }

        userRepository.save(user);
        return new APIResponse();
    }

    @Override
    public APIResponse updatePassword(String username, String oldPassword, String password) {

        UserEntity user = userRepository.findById(username)
                .orElseThrow(() -> new BadCredentialsException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀렸습니다.");
        }

        user.changePassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return new APIResponse();
    }

    @Override
    public APIResponse deleteUser(String username) {

        UserEntity user = userRepository.findById(username)
                .orElseThrow(() -> new BadCredentialsException("존재하지 않는 회원입니다."));

        user.deleteUser();
        userRepository.save(user);

        return new APIResponse();
    }

    @Override
    public APIResponse verifyEmail(String code) {

        UserEntity user = userRepository.findByVerifyCode(code)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 인증 코드입니다."));

        user.addRole(ROLE_USER);
        userRepository.save(user);

        return new APIResponse();
    }

    @Override
    public APIResponse checkEmail(String email) {
        validateEmail(email);

        return new APIResponse();
    }

    @Override
    public APIResponse sendResetPasswordLink(String email) {

        UserEntity userEntity = userRepository.findById(email)
                .orElse(null);

        if (userEntity != null) {
            String code = UUID.randomUUID().toString();
            ResetCodeStorage resetCodeStorage = new ResetCodeStorage(code, email);
            resetCodeStorageRedisRepository.save(resetCodeStorage);

            String resetPasswordUrl = String.format(appProperties.getResetPasswordUrl(), code);
            emailService.sendMessage(EmailMessage.of(email, "[UNI Online Judge] 비밀번호 재설정", resetPasswordUrl));
        }

        return new APIResponse();
    }

    @Override
    public APIResponse checkResetPasswordCode(String code) {

        resetCodeStorageRedisRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("코드가 유효하지 않습니다"));

        return new APIResponse();
    }

    @Override
    public APIResponse resetPassword(String code, String password) {

        ResetCodeStorage resetCodeStorage = resetCodeStorageRedisRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("코드가 유효하지 않습니다"));

        String email = resetCodeStorage.getUserId();
        UserEntity userEntity = userRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        userEntity.changePassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);

        resetCodeStorageRedisRepository.delete(resetCodeStorage);

        return new APIResponse();
    }

    private void validateEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new RuntimeException("올바르지 않은 Email 입니다.");
        }

        if (userRepository.existsById(email)) {
            throw new RuntimeException("이미 사용중인 Email 입니다.");
        }

        if (userRepository.existsByOAuthEmail(email)) {
            throw new RuntimeException("이미 사용중인 Email 입니다.");
        }
    }
}
