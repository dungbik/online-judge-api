package yoonleeverse.onlinejudge.api.common.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "common", description = "common API")
@RestController
@RequestMapping(path = "/common")
public class CommonController {

    @GetMapping("/health")
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok().build();
    }
}
