package pl.rafalcelinski.home_budget.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.rafalcelinski.home_budget.dto.RegisterDTO;
import pl.rafalcelinski.home_budget.dto.UserDTO;
import pl.rafalcelinski.home_budget.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        UserDTO registeredUserDTO = userService.registerNewUser(registerDTO);
        return ResponseEntity.ok(registeredUserDTO);
    }
}
