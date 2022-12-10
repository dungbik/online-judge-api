package yoonleeverse.onlinejudge.api.user.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import yoonleeverse.onlinejudge.api.common.constant.Constants;
import yoonleeverse.onlinejudge.api.common.entity.BaseTimeEntity;

 import java.util.List;
import java.util.Set;

@Getter
@Document(collection = "users")
@Builder
public class UserEntity extends BaseTimeEntity {
    @Id private String email;
    private String name;
    private String password;
    private String avatarUrl;
    private List<OAuthLink> links;
    private Set<Constants.ERole> roles;
    private boolean enabled;
    private String verifyCode;

    public void addOAuthLink(OAuthLink oAuthLink) {
        this.email = oAuthLink.getEmail();
        this.avatarUrl = oAuthLink.getAvatarUrl();
        this.links.add(oAuthLink);
    }

    public void addRole(Constants.ERole role) {
        this.roles.add(role);
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void deleteUser() {
        this.enabled = false;
    }
}
