package yoonleeverse.onlinejudge.api.submission.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentVO {

    private String submissionId;
    private String commentId;
    private String content;

    private String userId;
    private LocalDateTime createdAt;
}
