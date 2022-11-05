package yoonleeverse.onlinejudge.api.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;

@Data
public class SubmitProblemRequest {

    @Schema(example = "1")
    private long problemId;

    @Schema(description = "채점 가능 언어", enumAsRef = true)
    private ProgrammingLanguage language;

    @Schema(example = "#include <stdio.h>\n" +
            "int main(int argc, char *argv[]) {\n" +
            "    char input[1000];\n" +
            "    scanf(\"%s\", input);\n" +
            "    printf(\"Hello %s\\n\", input);\n" +
            "    char input2[1000];\n" +
            "    scanf(\"%s\", input2);\n" +
            "    printf(\"Hello2 %s\\n\", input2);\n" +
            "    return 0; \n" +
            "}")
    private String code;

    @Schema(example = "true", description = "채점 여부")
    private boolean isJudge;
}
