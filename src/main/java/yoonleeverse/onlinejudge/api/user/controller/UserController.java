package yoonleeverse.onlinejudge.api.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.user.dto.*;
import yoonleeverse.onlinejudge.api.user.service.UserService;
import yoonleeverse.onlinejudge.security.CurrentUser;
import yoonleeverse.onlinejudge.security.UserPrincipal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;

@Validated
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "현재 로그인된 유저 정보", security = { @SecurityRequirement(name = "Bearer") })
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public CurrentUserResponse getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {

        return userService.getCurrentUser(userPrincipal.getUsername());
    }

    @Operation(summary = "회원가입 요청")
    @PostMapping
    public SignUpResponse signUp(HttpServletResponse response, @RequestBody @Valid SignUpRequest req) {

        return userService.signUp(response, req);
    }

    @Operation(summary = "로그인 요청")
    @PostMapping("/login")
    public SignInResponse signIn(HttpServletResponse response, @RequestBody @Valid SignInRequest req) {

        return userService.signIn(response, req);
    }

    @Operation(summary = "이름 사용 가능 여부 체크")
    @GetMapping("/name/{name}")
    public APIResponse checkName(@PathVariable @Schema(description = "이름") String name) {

        return userService.checkName(name);
    }

    @Operation(summary = "로그아웃 요청", security = { @SecurityRequirement(name = "Bearer") })
    @PostMapping("/logout")
    public APIResponse signOut(HttpServletRequest request, HttpServletResponse response,
                               @CurrentUser UserPrincipal userPrincipal) {

        return userService.signOut(request, response, userPrincipal);
    }

    @Operation(summary = "만료된 토큰 갱신", security = { @SecurityRequirement(name = "Refresh Token") })
    @PostMapping("/refresh")
    public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        return userService.refreshToken(request, response);
    }

    @Operation(summary = "SNS 계정 연동", security = { @SecurityRequirement(name = "Bearer") })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/link/{linkKey}")
    public APIResponse addSnsAccount(@PathVariable String linkKey, @CurrentUser UserPrincipal userPrincipal) {

        return userService.addSnsAccount(linkKey, userPrincipal.getUsername());
    }

    @Operation(summary = "비밀번호 변경", security = { @SecurityRequirement(name = "Bearer") })
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/updatePassword")
    public APIResponse updatePassword(@CurrentUser UserPrincipal userPrincipal,
                                      @RequestParam String oldPassword, @RequestParam String password) {

        return userService.updatePassword(userPrincipal.getUsername(), oldPassword, password);
    }

    @Operation(summary = "회원 탈퇴", security = { @SecurityRequirement(name = "Bearer") })
    @PreAuthorize("isAuthenticated() and not hasRole('ADMIN')")
    @DeleteMapping
    public APIResponse deleteUser(@CurrentUser UserPrincipal userPrincipal) {

        return userService.deleteUser(userPrincipal.getUsername());
    }

    @Operation(summary = "이메일 인증")
    @GetMapping("/verify/{code}")
    public APIResponse verifyEmail(@PathVariable @Schema(description = "인증 코드") String code) {

        return userService.verifyEmail(code);
    }

    @Operation(summary = "이메일 중복 체크")
    @GetMapping("/email/{email}")
    public APIResponse checkEmail(@PathVariable @Schema(description = "이메일") @Email String email) {

        return userService.checkEmail(email);
    }

}
