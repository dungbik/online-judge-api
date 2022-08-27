package yoonleeverse.onlinejudge.api.user.dto;

import lombok.Data;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;

@Data
public class SignInResponse extends APIResponse {

    public static SignInResponse ofSuccess(String accessToken) {
        SignInResponse response = new SignInResponse();
        response.setSuccess(true);
        response.setAccessToken(accessToken);
        return response;
    }
}