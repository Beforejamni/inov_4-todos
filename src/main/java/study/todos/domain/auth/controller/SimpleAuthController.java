package study.todos.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.todos.domain.auth.dto.SimpleAuthReq;
import study.todos.domain.auth.dto.SimpleAuthRes;
import study.todos.domain.auth.dto.SimpleSignInReq;
import study.todos.domain.auth.dto.SimpleTokenDto;
import study.todos.domain.auth.service.SimpleAuthService;

@RestController
@RequestMapping("/auth")
public class SimpleAuthController {
    private final SimpleAuthService simpleAuthService;

    public SimpleAuthController(SimpleAuthService simpleAuthService) {
        this.simpleAuthService = simpleAuthService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SimpleAuthRes> signUp(@RequestBody SimpleAuthReq req) {
        return ResponseEntity.ok(simpleAuthService.signUp(req));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SimpleTokenDto> signIn(@RequestBody SimpleSignInReq req) {

        return ResponseEntity.ok(simpleAuthService.signIn(req));
    }

}
