package yoonleeverse.onlinejudge.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class SendResetLinkRequest {

    @Schema(example = "test1234@naver.com", description = "이메일")
    @Email
    @NotEmpty
    private String email;

}
