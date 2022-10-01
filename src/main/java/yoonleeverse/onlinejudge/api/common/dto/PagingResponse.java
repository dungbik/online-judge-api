package yoonleeverse.onlinejudge.api.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PagingResponse {
    private long totalElements;
    private long currentPages;
    private long totalPages;
    @JsonProperty("is_first") private boolean isFirst;
    @JsonProperty("is_last")  private boolean isLast;
}
