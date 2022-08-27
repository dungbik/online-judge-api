package yoonleeverse.onlinejudge.api.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/common")
public class CommonController {

    @GetMapping("/health")
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok().build();
    }
}
