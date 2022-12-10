package yoonleeverse.onlinejudge.api.submission.service;

import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.submission.dto.*;
import yoonleeverse.onlinejudge.security.UserPrincipal;

public interface SubmissionService {
    SubmitProblemResponse submitProblem(UserPrincipal userPrincipal, SubmitProblemRequest req);

    GetAllSubmissionResponse getAllSubmission(UserPrincipal userPrincipal, GetAllSubmissionRequest req);

    APIResponse addLike(String email, AddLikeRequest req);

    APIResponse removeLike(String email, AddLikeRequest req);
}
