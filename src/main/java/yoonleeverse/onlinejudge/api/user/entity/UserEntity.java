package yoonleeverse.onlinejudge.api.user.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import yoonleeverse.onlinejudge.api.common.entity.BaseTimeEntity;

import java.util.HashSet;
import java.util.Set;

@Getter
@Document(collection = "users")
@Builder
public class UserEntity extends BaseTimeEntity {

    @Id private String id;
    private String userId;
    private String name;
    private String email;
    private String password;
    private String avatarUrl;
    private String providerType;
    private Set<String> roles;

    public void changeName(String name) {
        this.name = name;
    }

    public void changeAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
