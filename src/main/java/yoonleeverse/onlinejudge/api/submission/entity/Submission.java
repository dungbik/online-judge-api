package yoonleeverse.onlinejudge.api.submission.entity;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import yoonleeverse.onlinejudge.api.common.entity.BaseTimeEntity;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;

import java.util.List;

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
    private Long memory; // byte
    private Integer realTime; // ms
    private Integer codeLength;

    private List<JudgeResult> resultList;
    private boolean isJudge;

    public void setStatus(JudgeStatus status) {
        setStatus(status, null, null, null);
    }

    public void setStatus(JudgeStatus status, Long memory, Integer realTime, List<JudgeResult> resultList) {
        this.status = status;
        this.memory = memory;
        this.realTime = realTime;
        this.resultList = resultList;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
