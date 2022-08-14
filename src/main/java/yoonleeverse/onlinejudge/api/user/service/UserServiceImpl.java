package yoonleeverse.onlinejudge.api.user.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinejudge.api.user.dto.*;
import yoonleeverse.onlinejudge.api.user.entity.OAuthLink;
import yoonleeverse.onlinejudge.api.user.entity.UserEntity;
import yoonleeverse.onlinejudge.api.user.repository.OAuthLinkRedisRepository;
import yoonleeverse.onlinejudge.api.user.repository.UserRepository;
import yoonleeverse.onlinejudge.security.AuthTokenProvider;

import java.util.ArrayList;
import java.util.Set;

import static yoonleeverse.onlinejudge.api.common.constant.Constants.ERole.ROLE_USER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuthLinkRedisRepository oAuthLinkRedisRepository;
    private final AuthTokenProvider authTokenProvider;

    @Override
    public SignUpResponse signUp(SignUpRequest req) {

        String id = req.getId();
        String password = req.getPassword();
        String name = req.getName();
        String linkKey = req.getLinkKey();

        boolean existIdOrName = userRepository.existsByIdOrName(id, name);
        if (existIdOrName) {
            throw new RuntimeException("이미 존재하는 ID 또는 닉네임입니다.");
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

        return SignUpResponse.ofSuccess(accessToken);
    }

    @Override
    public CurrentUserResponse getCurrentUser(String id) {

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("존재하지 않는 회원입니다."));
        return CurrentUserResponse.ofSuccess(userEntity);
    }

    @Override
    public SignInResponse signIn(SignInRequest req) {
        return null;
    }

}
