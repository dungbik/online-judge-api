package yoonleeverse.onlinejudge.api.submission.dto;

import lombok.Data;

@Data
public class AddCommentRequest {

    private String submissionId;
    private String content;
}
