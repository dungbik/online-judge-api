package yoonleeverse.onlinejudge.api.problem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCase implements Serializable {
    private int id;
    private String input;
    private String output;
    private String outputMD5;
}
