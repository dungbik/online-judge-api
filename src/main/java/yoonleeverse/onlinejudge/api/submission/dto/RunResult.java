package yoonleeverse.onlinejudge.api.submission.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RunResult implements Serializable {
    private int id;
    private int cpuTime;
    private int real_time;
    private long memory;
    private int signal;
    private int exit_code;
    private int error;
    private int result;
    private String output;
    private String outputMD5;
}
