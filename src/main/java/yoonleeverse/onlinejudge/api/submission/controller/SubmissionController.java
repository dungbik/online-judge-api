package yoonleeverse.onlinejudge.api.submission.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemRequest;
import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemResponse;
import yoonleeverse.onlinejudge.api.submission.service.SubmissionService;
import yoonleeverse.onlinejudge.security.CurrentUser;
import yoonleeverse.onlinejudge.security.UserPrincipal;

@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @Operation(summary = "문제 채점", security = { @SecurityRequirement(name = "Bearer") })
    @PostMapping
    public SubmitProblemResponse submitProblem(@CurrentUser UserPrincipal userPrincipal,
                                               @RequestBody SubmitProblemRequest req) {
         return submissionService.submitProblem(userPrincipal, req);
    }
}
