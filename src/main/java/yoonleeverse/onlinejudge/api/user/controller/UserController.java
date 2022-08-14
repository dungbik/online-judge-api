package yoonleeverse.onlinejudge.api.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yoonleeverse.onlinejudge.api.user.dto.*;
import yoonleeverse.onlinejudge.api.user.service.UserService;
import yoonleeverse.onlinejudge.security.CurrentUser;
import yoonleeverse.onlinejudge.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Parameter;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "현재 로그인된 유저 정보", security = { @SecurityRequirement(name = "Bearer") })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public CurrentUserResponse getCurrentUser(@CurrentUser @Parameter(hidden = true) UserPrincipal userPrincipal) {

        return userService.getCurrentUser(userPrincipal.getUsername());
    }

    @Operation(summary = "회원가입 요청")
    @PostMapping
    public SignUpResponse signUp(@RequestBody @Valid SignUpRequest req) {

        return userService.signUp(req);
    }

    @Operation(summary = "로그인 요청")
    @PostMapping("/login")
    public SignInResponse signIn(@RequestBody @Valid SignInRequest req) {

        return userService.signIn(req);
    }

}
