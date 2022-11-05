package yoonleeverse.onlinejudge.api.submission.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class JudgeResult {
    private int id;
    private int cpuTime;
    private int real_time;
    private long memory;
    private int signal;
    private int exit_code;
    private int error;
    private int result;
    private String output;
    @JsonIgnore
    private String outputMD5;
    private boolean isCorrect;
}
