package yoonleeverse.onlinejudge.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;

@Data
public class CheckNameRequest {

    @Schema(example = "멋쟁이", description = "이름")
    @NonNull
    @Size(min = 3, max = 10)
    private String name;
}
