package yoonleeverse.onlinejudge.api.submission.dto;

import lombok.Data;
import yoonleeverse.onlinejudge.api.common.dto.PagingResponse;

import java.util.List;

@Data
public class GetAllLikeResponse {

    private PagingResponse page;
    private List<LikeVO> likes;
}
