package yoonleeverse.onlinejudge.api.submission.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import yoonleeverse.onlinejudge.api.submission.entity.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String>, CustomCommentRepository {

    List<Comment> findAllBySubmissionIdAndDeleted(String submissionId, boolean isDeleted);

}
