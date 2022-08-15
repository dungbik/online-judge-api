package yoonleeverse.onlinejudge.api.user.dto;

import lombok.Data;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;

@Data
public class RefreshTokenResponse extends APIResponse {

    public static RefreshTokenResponse ofSuccess(String accessToken) {
        RefreshTokenResponse response = new RefreshTokenResponse();
        response.setSuccess(true);
        response.setAccessToken(accessToken);
        return response;
    }

    public static RefreshTokenResponse ofFail() {
        RefreshTokenResponse response = new RefreshTokenResponse();
        response.setSuccess(false);
        return response;
    }
}
