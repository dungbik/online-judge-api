package yoonleeverse.onlinejudge.api.submission.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.common.dto.PagingResponse;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class GetAllCommentResponse extends APIResponse {
    private PagingResponse page;
    private List<CommentVO> comments = new ArrayList<>();
}
