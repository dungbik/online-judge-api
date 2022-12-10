package yoonleeverse.onlinejudge.api.submission.dto;

import lombok.Data;

@Data
public class UpdateCommentRequest {

    private String commentId;
    private String content;
}
