package yoonleeverse.onlinejudge.security.oauth;

import yoonleeverse.onlinejudge.security.oauth.provider.GithubOAuth2UserInfo;
import yoonleeverse.onlinejudge.security.oauth.provider.GoogleOAuth2UserInfo;
import yoonleeverse.onlinejudge.security.oauth.provider.KakaoOAuth2UserInfo;
import yoonleeverse.onlinejudge.security.oauth.provider.ProviderType;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case GOOGLE: return new GoogleOAuth2UserInfo(attributes);
            case KAKAO: return new KakaoOAuth2UserInfo(attributes);
            case GITHUB: return new GithubOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}