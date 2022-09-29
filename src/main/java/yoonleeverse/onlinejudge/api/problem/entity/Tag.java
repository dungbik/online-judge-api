package yoonleeverse.onlinejudge.api.problem.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import yoonleeverse.onlinejudge.api.common.constant.MongoDB;
import yoonleeverse.onlinejudge.api.common.entity.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Document(collection = MongoDB.TAG)
@Builder
public class Tag extends BaseTimeEntity {
    @Id @Setter private Long id;
    private String name;
    @DBRef(lazy = true) private List<Problem> problems;

    public static Tag makeTag(long id, String name) {
        return Tag.builder()
                .id(id).name(name).problems(new ArrayList<>())
                .build();
    }

    public void addProblem(Problem problem) {
        this.problems.add(problem);
    }
}
