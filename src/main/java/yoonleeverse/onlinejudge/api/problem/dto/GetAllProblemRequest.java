package yoonleeverse.onlinejudge.api.problem.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yoonleeverse.onlinejudge.api.common.dto.PagingRequest;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class GetAllProblemRequest extends PagingRequest {
    private String title;
    private List<ProgrammingLanguage> languages;
    private List<Long> tags;
}
