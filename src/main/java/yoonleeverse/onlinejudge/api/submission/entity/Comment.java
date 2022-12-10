package yoonleeverse.onlinejudge.api.submission.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import yoonleeverse.onlinejudge.api.common.entity.BaseTimeEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class Comment extends BaseTimeEntity {

    @Id private String id;
    private String content;
    private String submissionId;
    private String userId;
    private boolean isDeleted;

    public static Comment of(String content, String submissionId, String userId) {
        return new Comment(null, content, submissionId, userId, false);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void update(String content) {
        this.content = content;
    }

}
