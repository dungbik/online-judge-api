package yoonleeverse.onlinejudge.api.submission.dto;

import lombok.Data;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;
import yoonleeverse.onlinejudge.api.submission.entity.JudgeStatus;

import java.time.LocalDateTime;

@Data
public class Submission {
    private String id;
    private String userId;
    private long problemId;
    private ProgrammingLanguage language;
    private JudgeStatus status;
    private Long memory; // byte
    private Integer realTime; // ms
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String code;
    private Integer codeLength;
}
