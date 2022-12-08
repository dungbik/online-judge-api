package yoonleeverse.onlinejudge.api.problem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;
import yoonleeverse.onlinejudge.api.problem.entity.TestCaseExample;

import java.util.List;

@Data
public class AddProblemRequest {
    @Schema(example = "Hello World", description = "문제 제목")
    private String title;

    @Schema(example = "10000", description = "시간 제한 (ms)")
    private int timeLimit;
    @Schema(example = "10000", description = "메모리 제한 (byte)")
    private int memoryLimit;

    @Schema(example = "첫번째 단어 입력시 Hello 첫번째 단어, 두번쨰 단어 입력시 Hello2 두번째 단어가 출력되게 하여라.", description = "문제 설명")
    private String desc;
    @Schema(example = "10자 이내의 단어가 엔터를 기준으로 두 번 입력된다.", description = "문제 입력 설명")
    private String inputDesc;
    @Schema(example = "첫번째 단어 입력시 Hello 첫번째 단어, 두번쨰 단어 입력시 Hello2 두번째 단어가 출력되어야 한다.", description = "문제 출력 설명")
    private String outputDesc;
    @Schema(description = "문제 입출력 예시")
    private List<TestCaseExample> testCaseExamples;
    @Schema(description = "채점 가능 언어", enumAsRef = true)
    private List<ProgrammingLanguage> languages;
    @Schema(description = "문제 유형")
    private List<String> tags;
    @Schema(description = "문제 난이도")
    private int level;
}
