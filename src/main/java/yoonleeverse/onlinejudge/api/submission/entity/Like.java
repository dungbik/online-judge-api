package yoonleeverse.onlinejudge.api.submission.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import yoonleeverse.onlinejudge.api.common.entity.BaseTimeEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "likes")
public class Like extends BaseTimeEntity {

    @Id private String id;
    private String userId;
    private String submissionId;

    public static Like of(String userId, String submissionId) {
        return new Like(null, userId, submissionId);
    }
}


