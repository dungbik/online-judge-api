package yoonleeverse.onlinejudge.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinejudge.config.AppProperties;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthTokenProvider {
    private Key key;
    private AppProperties.Auth auth;

    private final AppProperties appProperties;

    @PostConstruct
    protected void init() {
        this.auth = appProperties.getAuth();
        this.key = Keys.hmacShaKeyFor(this.auth.getTokenSecret().getBytes());
    }

    public String createAccessToken(String id) {
        return createToken(id, auth.getAccessTokenExp());
    }

    public String createRefreshToken(String id) {
        return createToken(id, auth.getRefreshTokenExp());
    }

    private String createToken(String id, long exp) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (exp * 1000));

        return Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .compact();
    }

    public Claims getTokenClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT token compact of handler are invalid.");
        }

        return null;
    }

}