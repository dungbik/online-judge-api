package yoonleeverse.onlinejudge.api.problem.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TestCaseExample {
    @Schema(example = "World\n" +
            "World2\n")
    private String input;
    @Schema(example = "Hello World\n" +
            "Hello2 World2\n")
    private String output;
}
