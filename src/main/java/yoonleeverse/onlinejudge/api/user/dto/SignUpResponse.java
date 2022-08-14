package yoonleeverse.onlinejudge.api.user.dto;

import lombok.Data;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;

@Data
public class SignUpResponse extends APIResponse {

    public static SignUpResponse ofSuccess(String accessToken) {
        SignUpResponse response = new SignUpResponse();
        response.setSuccess(true);
        response.setAccessToken(accessToken);
        return response;
    }
}
