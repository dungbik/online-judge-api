package yoonleeverse.onlinejudge.api.problem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinejudge.api.problem.entity.TestCaseExample;

import java.beans.PropertyEditorSupport;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TestCasePropertyEditor extends PropertyEditorSupport {

    private final ObjectMapper objectMapper;

    @Override
    public void setAsText(String text) {
        text = "[" + text + "]";
        try {
            List<TestCaseExample> testCaseExamples
                    = objectMapper.readValue(text, new TypeReference<List<TestCaseExample>>() {});
            setValue(testCaseExamples);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
