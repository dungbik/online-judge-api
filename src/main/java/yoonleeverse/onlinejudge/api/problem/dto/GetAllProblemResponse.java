package yoonleeverse.onlinejudge.api.problem.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.common.dto.PagingResponse;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class GetAllProblemResponse extends APIResponse {
    private PagingResponse page;
    private List<GetProblemResponse> problems = new ArrayList<>();
}
