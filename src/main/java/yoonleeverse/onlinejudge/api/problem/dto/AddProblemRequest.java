package yoonleeverse.onlinejudge.api.problem.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinejudge.api.problem.entity.TestCaseExample;

import java.util.List;

@Data
public class AddProblemRequest {
    private String title;

    private int timeLimit;
    private int memoryLimit;

    private String desc;
    private String inputDesc;
    private String outputDesc;
    private List<TestCaseExample> testCaseExamples;
    private List<String> languages;

    private MultipartFile file;
}
