package yoonleeverse.onlinejudge.api.problem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.problem.dto.GetAllTagResponse;
import yoonleeverse.onlinejudge.api.problem.dto.*;
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

    @Operation(summary = "문제 상세 정보 보기")
    @GetMapping("/{id}")
    public GetProblemResponse getProblem(@PathVariable Long id) {
        return problemService.getProblem(id);
    }

    @Operation(summary = "문제 검색")
    @GetMapping
    public GetAllProblemResponse getAllProblem(GetAllProblemRequest req) {
        return problemService.getAllProblem(req);
    }

    @Operation(summary = "문제 삭제", security = { @SecurityRequirement(name = "Bearer") })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public APIResponse removeProblem(@CurrentUser UserPrincipal userPrincipal,
                                     @PathVariable Long id) {
        return problemService.removeProblem(userPrincipal, id);
    }

    @Operation(summary = "문제 수정", security = { @SecurityRequirement(name = "Bearer") })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public APIResponse updateProblem(@CurrentUser UserPrincipal userPrincipal,
                                     @PathVariable Long id,
                                     @RequestPart AddProblemRequest req,
                                     @RequestPart MultipartFile file) {
        return problemService.updateProblem(userPrincipal, id, req, file);
    }

    @Operation(summary = "태그 리스트 조회")
    @GetMapping("/tags")
    public GetAllTagResponse getAllTag() {
        return problemService.getAllTag();
    }

}
