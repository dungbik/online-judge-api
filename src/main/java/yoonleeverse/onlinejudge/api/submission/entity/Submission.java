package yoonleeverse.onlinejudge.api.submission.entity;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import yoonleeverse.onlinejudge.api.common.entity.BaseTimeEntity;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;

@Getter
@Document(collection = "submissions")
@Builder
public class Submission extends BaseTimeEntity {
    @Id private String id;
    private String userId;
    private long problemId;
    private String code;
    private ProgrammingLanguage language;
    private JudgeStatus status;

    public void setStatus(JudgeStatus status) {
        this.status = status;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
