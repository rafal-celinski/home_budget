package pl.rafalcelinski.home_budget.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.rafalcelinski.home_budget.dto.TokenDTO;
import pl.rafalcelinski.home_budget.dto.LoginDTO;
import pl.rafalcelinski.home_budget.service.AuthorizationService;


@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthorizationService authorizationService;

    LoginController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        TokenDTO tokenDTO = authorizationService.authenticate(loginDTO);
        return ResponseEntity.ok(tokenDTO);
    }
}
