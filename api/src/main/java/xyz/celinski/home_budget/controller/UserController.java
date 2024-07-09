package xyz.celinski.home_budget.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.celinski.home_budget.dto.HttpErrorDTO;
import xyz.celinski.home_budget.dto.RegisterDTO;
import xyz.celinski.home_budget.dto.UserDTO;
import xyz.celinski.home_budget.exception.InvalidUserDetailsException;
import xyz.celinski.home_budget.exception.UserAlreadyExistsException;
import xyz.celinski.home_budget.service.UserService;
import xyz.celinski.home_budget.model.User;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        String email = registerDTO.getEmail();
        String passwordHash = registerDTO.getPasswordHash();

        User newUser = new User(email, passwordHash);

        try {
            User registeredUser = userService.registerNewUser(newUser);
            return ResponseEntity.ok(new UserDTO(registeredUser.getEmail()));
            // TODO: Entity->DTO converter
        }
        catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new HttpErrorDTO(new Date(), HttpStatus.CONFLICT.value(), e.getMessage(), "/user/register"));
        }
        catch (InvalidUserDetailsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new HttpErrorDTO(new Date(), HttpStatus.BAD_REQUEST.value(), e.getMessage(), "/user/register"));
        }
    }
}
