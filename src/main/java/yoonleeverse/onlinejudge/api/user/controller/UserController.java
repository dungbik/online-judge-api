package yoonleeverse.onlinejudge.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yoonleeverse.onlinejudge.api.user.entity.UserEntity;
import yoonleeverse.onlinejudge.api.user.repository.UserRepository;
import yoonleeverse.onlinejudge.security.CurrentUser;
import yoonleeverse.onlinejudge.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public UserEntity getCurrentUser(@CurrentUser @Parameter(hidden = true) UserPrincipal userPrincipal) {

        return userRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }
}
