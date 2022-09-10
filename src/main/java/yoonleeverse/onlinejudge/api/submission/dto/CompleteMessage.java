package yoonleeverse.onlinejudge.api.submission.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CompleteMessage implements Serializable {
    private String submissionId;
    private long problemId;
    private List<RunResult> results;
}
