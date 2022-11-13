package yoonleeverse.onlinejudge.api.submission.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yoonleeverse.onlinejudge.api.submission.dto.GetAllSubmissionRequest;
import yoonleeverse.onlinejudge.api.submission.dto.GetAllSubmissionResponse;
import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemRequest;
import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemResponse;
import yoonleeverse.onlinejudge.api.submission.service.SubmissionService;
import yoonleeverse.onlinejudge.security.CurrentUser;
import yoonleeverse.onlinejudge.security.UserPrincipal;

import java.util.Map;

@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "문제 채점", security = { @SecurityRequirement(name = "Bearer") })
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public SubmitProblemResponse submitProblem(@CurrentUser UserPrincipal userPrincipal,
                                               @RequestBody SubmitProblemRequest req) {
         return submissionService.submitProblem(userPrincipal, req);
    }

    @Operation(summary = "채점 제출 이력 보기")
    @GetMapping
    public GetAllSubmissionResponse getAllSubmission(@RequestParam @Parameter(hidden = true) Map<String, String> params,
                                                     GetAllSubmissionRequest dummy) {
        GetAllSubmissionRequest req = this.objectMapper.convertValue(params, GetAllSubmissionRequest.class);
        return this.submissionService.getAllSubmission(req);
    }

}
