package yoonleeverse.onlinejudge.api.submission.dto;

import lombok.Data;

@Data
public class LikeVO {
    private String submissionId;
    private SubmissionVO submission;
}
