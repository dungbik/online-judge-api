package yoonleeverse.onlinejudge.api.submission.repository;

import org.springframework.data.domain.Page;
import yoonleeverse.onlinejudge.api.submission.dto.GetAllSubmissionRequest;
import yoonleeverse.onlinejudge.api.submission.entity.Submission;

public interface CustomSubmissionRepository {
    Page<Submission> getAllSubmission(GetAllSubmissionRequest req);
}
