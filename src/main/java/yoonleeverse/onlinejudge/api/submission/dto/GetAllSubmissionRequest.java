package yoonleeverse.onlinejudge.api.submission.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import yoonleeverse.onlinejudge.api.common.dto.PagingRequest;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;

@Data
@EqualsAndHashCode(callSuper = false)
public class GetAllSubmissionRequest extends PagingRequest {

    @Schema(example = "1", description = "채점한 문제 번호")
    private Long problemId;

    @Schema(description = "사용한 언어", enumAsRef = true)
    private ProgrammingLanguage language;

    @Schema(example = "test1234", description = "제출한 유저 아이디")
    private String userId;

    @Schema(example = "false", description = "랭킹 적용 여부")
    @JsonProperty("is_ranking")
    private boolean isRanking;
}
