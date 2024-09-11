package pl.rafalcelinski.home_budget.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.rafalcelinski.home_budget.dto.TokenDTO;
import pl.rafalcelinski.home_budget.dto.LoginDTO;
import pl.rafalcelinski.home_budget.service.AuthService;


@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        TokenDTO tokenDTO = authService.login(loginDTO);
        return ResponseEntity.ok(tokenDTO);
    }
}
