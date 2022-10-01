package yoonleeverse.onlinejudge.api.problem.service;

import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemRequest;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemResponse;
import yoonleeverse.onlinejudge.api.problem.dto.GetProblemResponse;
import yoonleeverse.onlinejudge.security.UserPrincipal;

public interface ProblemService {
    AddProblemResponse addProblem(UserPrincipal userPrincipal, AddProblemRequest req, MultipartFile file);
    GetProblemResponse getProblem(Long id);
}
