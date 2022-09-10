package yoonleeverse.onlinejudge.api.submission.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import yoonleeverse.onlinejudge.api.submission.entity.Submission;

public interface SubmissionRepository extends MongoRepository<Submission, String>, CustomSubmissionRepository {
}
