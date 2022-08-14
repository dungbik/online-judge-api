package yoonleeverse.onlinejudge.security.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class LinkOAuth2User implements OAuth2User {
    private String name;
    private Map<String, Object> attributes;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean newUser;
    private String linkKey;

    public LinkOAuth2User(OAuth2User user, boolean newUser, String linkKey) {
        this.name = user.getName();
        this.attributes = user.getAttributes();
        this.authorities = user.getAuthorities();
        this.newUser = newUser;
        this.linkKey = linkKey;
    }

}
