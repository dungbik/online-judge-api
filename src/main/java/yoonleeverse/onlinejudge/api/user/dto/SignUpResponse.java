package yoonleeverse.onlinejudge.api.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;

@Data
@EqualsAndHashCode(callSuper=false)
public class SignUpResponse extends APIResponse {

    public static SignUpResponse ofSuccess(String accessToken) {
        SignUpResponse response = new SignUpResponse();
        response.setSuccess(true);
        response.setAccessToken(accessToken);
        return response;
    }
}
