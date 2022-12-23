package yoonleeverse.onlinejudge.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinejudge.api.user.entity.TokenStorage;
import yoonleeverse.onlinejudge.api.user.repository.TokenStorageRedisRepository;
import yoonleeverse.onlinejudge.api.user.repository.UserRepository;
import yoonleeverse.onlinejudge.config.AppProperties;
import yoonleeverse.onlinejudge.security.AuthTokenProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

import static yoonleeverse.onlinejudge.api.common.constant.Constants.Cookie.REFRESH_TOKEN;

@Component
@RequiredArgsConstructor
public class UserComponent {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[가-힣|a-z|A-Z|0-9]+");

    private final UserRepository userRepository;
    private final AuthTokenProvider authTokenProvider;
    private final AppProperties appProperties;
    private final TokenStorageRedisRepository tokenStorageRedisRepository;

    /**
     * 이름 사용 가능 여부 체크
     * @param name 이름
     * @return 이름 사용 가능 여부
     */
    public boolean checkName(String name) {
        if (userRepository.existsByName(name)) {
            return false;
        }

        if (!NAME_PATTERN.matcher(name).matches()) {
            return false;
        }

        if (name.length() < 3 || name.length() > 10) {
            return false;
        }

        return true;
    }

    /**
     * 토큰 발급
     * @param response HttpServletResponse
     * @param email Email
     * @return accessToken
     */
    public String issueToken(HttpServletResponse response, String email) {
        String accessToken = authTokenProvider.createAccessToken(email);
        String refreshToken = authTokenProvider.createRefreshToken(email);

        TokenStorage tokenStorage = new TokenStorage(refreshToken, email);
        tokenStorageRedisRepository.save(tokenStorage);

        int refreshTokenExp = (int) appProperties.getAuth().getRefreshTokenExp();

        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(refreshTokenExp)
                .sameSite(Cookie.SameSite.NONE.name())
                .secure(true)
                .domain("yoonleeverse.com")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return accessToken;
    }
}
