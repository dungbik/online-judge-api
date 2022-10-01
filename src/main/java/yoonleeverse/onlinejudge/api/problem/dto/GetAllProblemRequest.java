package yoonleeverse.onlinejudge.api.problem.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yoonleeverse.onlinejudge.api.common.dto.PagingRequest;

@Data
@EqualsAndHashCode(callSuper=false)
public class GetAllProblemRequest extends PagingRequest {
    private String title;
}
