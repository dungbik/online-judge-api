package yoonleeverse.onlinejudge.api.user.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.user.UserComponent;
import yoonleeverse.onlinejudge.api.user.dto.*;
import yoonleeverse.onlinejudge.api.user.entity.OAuthLink;
import yoonleeverse.onlinejudge.api.user.entity.TokenStorage;
import yoonleeverse.onlinejudge.api.user.entity.UserEntity;
import yoonleeverse.onlinejudge.api.user.repository.OAuthLinkRedisRepository;
import yoonleeverse.onlinejudge.api.user.repository.TokenStorageRedisRepository;
import yoonleeverse.onlinejudge.api.user.repository.UserRepository;
import yoonleeverse.onlinejudge.config.AppProperties;
import yoonleeverse.onlinejudge.security.AuthTokenProvider;
import yoonleeverse.onlinejudge.util.CookieUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Set;

import static yoonleeverse.onlinejudge.api.common.constant.Constants.Cookie.REFRESH_TOKEN;
import static yoonleeverse.onlinejudge.api.common.constant.Constants.ERole.ROLE_USER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuthLinkRedisRepository oAuthLinkRedisRepository;
    private final AuthTokenProvider authTokenProvider;
    private final AppProperties appProperties;
    private final TokenStorageRedisRepository tokenStorageRedisRepository;
    private final UserComponent userComponent;

    @Override
    public SignUpResponse signUp(HttpServletResponse response, SignUpRequest req) {

        String id = req.getId();
        String password = req.getPassword();
        String name = req.getName();
        String linkKey = req.getLinkKey();

        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("이미 사용중인 ID 입니다."));

        boolean checkName = userComponent.checkName(name);
        if (!checkName) {
            throw new RuntimeException("사용할 수 없는 닉네임입니다.");
        }

        UserEntity user = UserEntity.builder()
                .id(id)
                .name(name)
                .password(passwordEncoder.encode(password))
                .roles(Set.of(ROLE_USER.name()))
                .links(new ArrayList<>())
                .build();

        if (StringUtils.isNotEmpty(linkKey)) {
            OAuthLink oAuthLink = oAuthLinkRedisRepository.findById(linkKey)
                    .orElseThrow(() -> new RuntimeException("OAuth 인증 정보가 존재하지 않습니다."));

            user.addOAuthLink(oAuthLink);
            oAuthLinkRedisRepository.delete(oAuthLink);
        }

        userRepository.save(user);

        String accessToken = authTokenProvider.createAccessToken(id);
        String refreshToken = authTokenProvider.createRefreshToken(id);

        TokenStorage tokenStorage = new TokenStorage(id, accessToken, refreshToken);
        tokenStorageRedisRepository.save(tokenStorage);

        int refreshTokenExp = (int) appProperties.getAuth().getRefreshTokenExp();
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken, refreshTokenExp);

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

        String id = req.getId();
        String password = req.getPassword();
        String linkKey = req.getLinkKey();

        UserEntity user;

        if (StringUtils.isNotEmpty(linkKey)) {
            OAuthLink oAuthLink = oAuthLinkRedisRepository.findById(linkKey)
                    .orElseThrow(() -> new RuntimeException("OAuth 인증 정보가 존재하지 않습니다."));

            user = userRepository.findByProviderAndUserId(oAuthLink.getProvider(), oAuthLink.getUserId())
                    .orElseThrow(() -> new BadCredentialsException("존재하지 않는 회원입니다."));
        } else {
            user = userRepository.findById(id)
                    .orElseThrow(() -> new BadCredentialsException("존재하지 않는 회원입니다."));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("비밀번호가 틀렸습니다.");
            }
        }

        String accessToken = authTokenProvider.createAccessToken(user.getId());
        String refreshToken = authTokenProvider.createRefreshToken(user.getId());

        TokenStorage tokenStorage = new TokenStorage(user.getId(), accessToken, refreshToken);
        tokenStorageRedisRepository.save(tokenStorage);

        int refreshTokenExp = (int) appProperties.getAuth().getRefreshTokenExp();
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken, refreshTokenExp);

        return SignInResponse.ofSuccess(accessToken);
    }

    @Override
    public APIResponse checkName(CheckNameRequest req) {

        String name = req.getName();

        APIResponse response = new APIResponse();
        response.setSuccess(userComponent.checkName(name));

        return response;
    }

}
