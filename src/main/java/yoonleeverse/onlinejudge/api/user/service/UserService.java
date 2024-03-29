package yoonleeverse.onlinejudge.api.user.service;

import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.user.dto.*;
import yoonleeverse.onlinejudge.security.UserPrincipal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    SignUpResponse signUp(HttpServletResponse response, SignUpRequest req);

    CurrentUserResponse getCurrentUser(String id);

    SignInResponse signIn(HttpServletResponse response, SignInRequest req);

    APIResponse checkName(String name);

    APIResponse signOut(HttpServletRequest request, HttpServletResponse response, UserPrincipal userPrincipal);

    RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response);

    APIResponse addSnsAccount(String linkKey, String username);

    APIResponse updatePassword(String username, String oldPassword, String password);

    APIResponse deleteUser(String username);

    APIResponse verifyEmail(String code);

    APIResponse checkEmail(String email);

    APIResponse sendResetPasswordLink(String email);

    APIResponse checkResetPasswordCode(String code);

    APIResponse resetPassword(String code, String password);
}
