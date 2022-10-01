package yoonleeverse.onlinejudge.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;

@Data
@EqualsAndHashCode(callSuper=false)
public class SignInResponse extends APIResponse {

    @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2MmRkN2U2NTY0MWYzMDI3OTZjNTMyZDciLCJpYXQiOjE2NTkyNTkxMjMsImV4cCI6MTY2MDEyMzEyM30.cyTvifMKLqh4UeMAgl9E_QY5MBtJWeSf-XgdcIFdW8OLq7YFygcdE7dXFFX_KhjZL4oApjn5iYAeh0KUQKEgNA", description = "새로 발급된 accessToken")
    private String accessToken;

    public static SignInResponse ofSuccess(String accessToken) {
        SignInResponse response = new SignInResponse();
        response.setSuccess(true);
        response.setAccessToken(accessToken);
        return response;
    }
}
