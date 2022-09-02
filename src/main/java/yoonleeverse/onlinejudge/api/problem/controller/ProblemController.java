package yoonleeverse.onlinejudge.api.problem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import yoonleeverse.onlinejudge.api.problem.TestCasePropertyEditor;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemRequest;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemResponse;
import yoonleeverse.onlinejudge.api.problem.service.ProblemService;
import yoonleeverse.onlinejudge.security.CurrentUser;
import yoonleeverse.onlinejudge.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final TestCasePropertyEditor testCasePropertyEditor;
    private final ProblemService problemService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(List.class, "testCaseExamples", testCasePropertyEditor);
    }

    @Operation(summary = "문제 등록", security = { @SecurityRequirement(name = "Bearer") })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public AddProblemResponse addProblem(@CurrentUser UserPrincipal userPrincipal,
                                         @ModelAttribute AddProblemRequest req) {
        return problemService.addProblem(userPrincipal, req);
    }
}
