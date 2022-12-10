package yoonleeverse.onlinejudge.api.user.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import yoonleeverse.onlinejudge.api.common.constant.Constants;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserDTO {

    @Schema(example = "testName", description = "name")
    private String name;

    @Schema(example = "test123@naver.com", description = "Email")
    private String email;

    @Schema(example = "https://avatarUrl", description = "avatarUrl")
    private String avatarUrl;

    @Schema(description = "연동된 소셜 계정")
    private List<OAuthLinkDTO> links;

    @Schema(description = "권한", enumAsRef = true)
    private Set<Constants.ERole> roles;
}
