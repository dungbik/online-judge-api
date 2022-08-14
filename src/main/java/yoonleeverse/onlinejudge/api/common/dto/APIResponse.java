package yoonleeverse.onlinejudge.api.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class APIResponse {

    @Schema(example = "true", description = "API 요청 처리 성공 여부")
    private boolean success;

    @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2MmRkN2U2NTY0MWYzMDI3OTZjNTMyZDciLCJpYXQiOjE2NTkyNTkxMjMsImV4cCI6MTY2MDEyMzEyM30.cyTvifMKLqh4UeMAgl9E_QY5MBtJWeSf-XgdcIFdW8OLq7YFygcdE7dXFFX_KhjZL4oApjn5iYAeh0KUQKEgNA", description = "새로 발급된 accessToken")
    @JsonProperty("access_token")
    private String accessToken;
}
