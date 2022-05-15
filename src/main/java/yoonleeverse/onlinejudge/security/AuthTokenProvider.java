package yoonleeverse.onlinejudge.security;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinejudge.config.AppProperties;

import java.security.Key;

@Service
public class AuthTokenProvider {
    private final Key key;
    private AppProperties.Auth auth;

    public AuthTokenProvider(AppProperties appProperties) {
        this.auth = appProperties.getAuth();
        this.key = Keys.hmacShaKeyFor(this.auth.getTokenSecret().getBytes());
    }

    public AuthToken createAuthToken(String email) {
        return new AuthToken(email, this.auth.getTokenExpirationMsec(), key);
    }
    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }
}