package xyz.celinski.home_budget.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.celinski.home_budget.dto.HttpErrorDTO;
import xyz.celinski.home_budget.dto.TokenDTO;
import xyz.celinski.home_budget.exception.InvalidCredentialsException;
import xyz.celinski.home_budget.exception.UserNotFoundException;
import xyz.celinski.home_budget.dto.LoginDTO;
import xyz.celinski.home_budget.service.AuthService;

import java.util.Date;


@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            TokenDTO tokenDTO = authService.login(loginDTO);
            return ResponseEntity.ok(tokenDTO);
        }
        catch (UserNotFoundException | InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new HttpErrorDTO(new Date(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "/login"));
        }
    }
}
