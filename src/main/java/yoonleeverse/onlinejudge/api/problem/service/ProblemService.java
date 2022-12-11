package yoonleeverse.onlinejudge.api.problem.service;

import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.problem.dto.GetAllTagResponse;
import yoonleeverse.onlinejudge.api.problem.dto.*;
import yoonleeverse.onlinejudge.security.UserPrincipal;

public interface ProblemService {
    AddProblemResponse addProblem(UserPrincipal userPrincipal, AddProblemRequest req, MultipartFile file);

    GetProblemResponse getProblem(Long id);

    GetAllProblemResponse getAllProblem(GetAllProblemRequest req);

    APIResponse removeProblem(UserPrincipal userPrincipal, Long id);

    APIResponse updateProblem(UserPrincipal userPrincipal, Long id, AddProblemRequest req, MultipartFile file);

    GetAllTagResponse getAllTag();
}
