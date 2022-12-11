package yoonleeverse.onlinejudge.api.problem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TagVO {
    private long id;
    private String name;

    public static TagVO of(long id, String name) {
        return new TagVO(id, name);
    }
}
