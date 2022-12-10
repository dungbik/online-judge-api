package yoonleeverse.onlinejudge.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class ResetPasswordRequest {

    @Schema(example = "e1d0485f-a313-4c5c-b94d-a6117439f9c9", description = "코드")
    @NotEmpty
    private String code;

    @Schema(example = "test1234", description = "비밀번호")
    @Size(min = 6, max = 20)
    private String password;

}
