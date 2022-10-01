package yoonleeverse.onlinejudge.api.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class APIResponse {

    @Schema(example = "true", description = "API 요청 처리 성공 여부")
    private boolean success = true;

    private String errMsg;
}
