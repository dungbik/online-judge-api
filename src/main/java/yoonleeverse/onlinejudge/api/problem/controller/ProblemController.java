package yoonleeverse.onlinejudge.api.problem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemRequest;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemResponse;
import yoonleeverse.onlinejudge.api.problem.dto.GetProblemResponse;
import yoonleeverse.onlinejudge.api.problem.service.ProblemService;
import yoonleeverse.onlinejudge.security.CurrentUser;
import yoonleeverse.onlinejudge.security.UserPrincipal;

@RestController
@RequestMapping("/problems")
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;

    @Operation(summary = "문제 등록", security = { @SecurityRequirement(name = "Bearer") })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public AddProblemResponse addProblem(@CurrentUser UserPrincipal userPrincipal,
                                         @RequestPart AddProblemRequest req,
                                         @RequestPart MultipartFile file) {
        return problemService.addProblem(userPrincipal, req, file);
    }

    @GetMapping("/{id}")
    public GetProblemResponse getProblem(@PathVariable Long id) {
        return problemService.getProblem(id);
    }

}
