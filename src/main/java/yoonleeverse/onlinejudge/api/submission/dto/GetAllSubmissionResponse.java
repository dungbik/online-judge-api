package yoonleeverse.onlinejudge.api.submission.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.common.dto.PagingResponse;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class GetAllSubmissionResponse extends APIResponse {
    private PagingResponse page;
    private List<SubmissionVO> submissions;
}
