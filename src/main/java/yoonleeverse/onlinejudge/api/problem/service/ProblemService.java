package yoonleeverse.onlinejudge.api.problem.service;

import yoonleeverse.onlinejudge.api.problem.dto.AddProblemRequest;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemResponse;
import yoonleeverse.onlinejudge.security.UserPrincipal;

public interface ProblemService {
    AddProblemResponse addProblem(UserPrincipal userPrincipal, AddProblemRequest req);
}
