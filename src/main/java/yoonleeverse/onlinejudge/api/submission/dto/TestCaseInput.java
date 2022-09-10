package yoonleeverse.onlinejudge.api.submission.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TestCaseInput implements Serializable {
    private int id;
    private String input;
}
