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
    @Id private String id;
    private String name;
    private String password;
    private String avatarUrl;
    private List<OAuthLink> links;
    private Set<Constants.ERole> roles;

    public void addOAuthLink(OAuthLink oAuthLink) {
        this.avatarUrl = oAuthLink.getAvatarUrl();
        this.links.add(oAuthLink);
    }
}
