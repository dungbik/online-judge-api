package yoonleeverse.onlinejudge.security.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinejudge.api.user.entity.UserEntity;
import yoonleeverse.onlinejudge.api.user.repository.UserRepository;
import yoonleeverse.onlinejudge.security.UserPrincipal;

import java.util.Set;

import static yoonleeverse.onlinejudge.api.common.constant.Constants.ERole.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }
    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        UserEntity savedUserEntity = userRepository.findByEmail(userInfo.getEmail()).orElse(null);

        if (savedUserEntity != null) {
            if (!providerType.name().equals(savedUserEntity.getProviderType())) {
                throw new RuntimeException(
                        "Looks like you're signed up with " + providerType +
                                " account. Please use your " + savedUserEntity.getProviderType() + " account to login."
                );
            }
            updateUser(savedUserEntity, userInfo);
        } else {
            savedUserEntity = createUser(userInfo, providerType);
        }

        return UserPrincipal.create(savedUserEntity, user.getAttributes());
    }

    private UserEntity createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        UserEntity userEntity = UserEntity.builder()
                .userId(userInfo.getId())
                .name(userInfo.getName())
                .email(userInfo.getEmail())
                .avatarUrl(userInfo.getImageUrl())
                .providerType(providerType.name())
                .roles(Set.of(ROLE_USER.name()))
                .build();

        return userRepository.save(userEntity);
    }

    private UserEntity updateUser(UserEntity userEntity, OAuth2UserInfo userInfo) {
        userEntity.changeName(userInfo.getName());
        userEntity.changeAvatarUrl(userInfo.getImageUrl());

        return userRepository.save(userEntity);
    }
}