package yoonleeverse.onlinejudge.api.submission.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.submission.dto.*;
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

    @Operation(summary = "채점 제출 이력 보기", security = { @SecurityRequirement(name = "Bearer") })
    @GetMapping
    public GetAllSubmissionResponse getAllSubmission(@CurrentUser UserPrincipal userPrincipal,
                                                     @RequestParam @Parameter(hidden = true) Map<String, String> params,
                                                     GetAllSubmissionRequest dummy) {
        GetAllSubmissionRequest req = this.objectMapper.convertValue(params, GetAllSubmissionRequest.class);
        return this.submissionService.getAllSubmission(userPrincipal, req);
    }

    @Operation(summary = "제출 이력 좋아요 추가", security = { @SecurityRequirement(name = "Bearer") })
    @PostMapping("/like")
    @PreAuthorize("hasRole('USER')")
    public APIResponse addLike(@CurrentUser UserPrincipal userPrincipal,
                               @RequestBody AddLikeRequest req) {

        return submissionService.addLike(userPrincipal.getEmail(), req);
    }

    @Operation(summary = "제출 이력 좋아요 제거", security = { @SecurityRequirement(name = "Bearer") })
    @DeleteMapping("/like")
    @PreAuthorize("hasRole('USER')")
    public APIResponse removeLike(@CurrentUser UserPrincipal userPrincipal,
                               @RequestBody AddLikeRequest req) {

        return submissionService.removeLike(userPrincipal.getEmail(), req);
    }

}
