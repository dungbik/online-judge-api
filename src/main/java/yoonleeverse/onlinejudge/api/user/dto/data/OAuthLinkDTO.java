package yoonleeverse.onlinejudge.api.user.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthLinkDTO {

    @Schema(example = "GOOGLE", description = "소셜 계정 제공자")
    private String provider;

    @Schema(example = "test@google.com", description = "소셜 계정 email")
    private String email;

    @Schema(example = "https://lh3.googleusercontent.com/a-/AFdZucpujZmditgDFUcNHA4i4oF1tJQf4v4e0TcQgSlvow=s96-c",
            description = "소셜 계정 avatarUrl")
    private String avatarUrl;
}
