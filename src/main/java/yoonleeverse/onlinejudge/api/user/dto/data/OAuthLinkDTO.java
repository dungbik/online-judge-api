package yoonleeverse.onlinejudge.api.user.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthLinkDTO {
    private String provider;
    private String email;
    private String avatarUrl;
}
