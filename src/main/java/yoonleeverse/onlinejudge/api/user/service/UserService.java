package yoonleeverse.onlinejudge.api.user.service;

import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.user.dto.*;

import javax.servlet.http.HttpServletResponse;

public interface UserService {
    SignUpResponse signUp(HttpServletResponse response, SignUpRequest req);

    CurrentUserResponse getCurrentUser(String id);

    SignInResponse signIn(HttpServletResponse response, SignInRequest req);

    APIResponse checkName(CheckNameRequest req);
}
