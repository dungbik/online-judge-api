package yoonleeverse.onlinejudge.api.problem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetAllTagResponse extends APIResponse {
    private List<TagVO> tags;
}
