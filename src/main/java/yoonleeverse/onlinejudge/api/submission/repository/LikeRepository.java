package yoonleeverse.onlinejudge.api.submission.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import yoonleeverse.onlinejudge.api.submission.entity.Like;

import java.util.Optional;

public interface LikeRepository extends MongoRepository<Like, String>, CustomLikeRepository {

    Optional<Like> findBySubmissionIdAndUserId(String submissionId, String email);

}
