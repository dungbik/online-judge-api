package yoonleeverse.onlinejudge.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SignInRequest {

    @Schema(example = "test1234", description = "아이디")
//    @Size(min = 5, max = 20)
    private String id;

    @Schema(example = "test1234", description = "비밀번호")
//    @Size(min = 6, max = 20)
    private String password;

    @Schema(example = "e5410da7-e23a-4409-8d13-57531eb26332", description = "소셜 계정 로그인 키")
    private String linkKey;
}
