package yoonleeverse.onlinejudge.api.submission.service;

import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemRequest;
import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemResponse;
import yoonleeverse.onlinejudge.security.UserPrincipal;

public interface SubmissionService {
    SubmitProblemResponse submitProblem(UserPrincipal userPrincipal, SubmitProblemRequest req);
}
