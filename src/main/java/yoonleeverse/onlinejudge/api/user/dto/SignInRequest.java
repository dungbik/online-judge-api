package yoonleeverse.onlinejudge.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;

@Data
public class SignInRequest {

    @Schema(example = "test1234", description = "아이디")
    @NonNull
    @Size(min = 6, max = 20)
    private String id;

    @Schema(example = "test1234", description = "비밀번호")
    @NonNull
    @Size(min = 6, max = 20)
    private String password;
}
