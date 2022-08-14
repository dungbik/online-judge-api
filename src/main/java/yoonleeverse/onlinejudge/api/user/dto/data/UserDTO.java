package yoonleeverse.onlinejudge.api.user.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {

    @Schema(example = "test123", description = "ID")
    private String id;

    @Schema(example = "testName", description = "name")
    private String name;

    @Schema(example = "https://avatarUrl", description = "avatarUrl")
    private String avatarUrl;

    @Schema(description = "연동된 소셜 계정")
    private List<OAuthLinkDTO> links;
}
