package yoonleeverse.onlinejudge.api.user.service;

import yoonleeverse.onlinejudge.api.user.dto.*;

public interface UserService {
    SignUpResponse signUp(SignUpRequest req);

    CurrentUserResponse getCurrentUser(String id);

    SignInResponse signIn(SignInRequest req);
}
