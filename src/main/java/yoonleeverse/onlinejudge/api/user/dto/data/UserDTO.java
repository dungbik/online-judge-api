package yoonleeverse.onlinejudge.api.user.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String name;
    private String avatarUrl;
    private List<OAuthLinkDTO> links;
}
