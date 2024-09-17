package pl.rafalcelinski.home_budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.rafalcelinski.home_budget.dto.LoginDTO;
import pl.rafalcelinski.home_budget.dto.TokenDTO;
import pl.rafalcelinski.home_budget.exception.InvalidCredentialsException;
import pl.rafalcelinski.home_budget.exception.UserNotFoundException;
import pl.rafalcelinski.home_budget.service.AuthorizationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthorizationService authorizationService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void loginEndpoint_shouldReturnToken_whenCredentialsAreValid() throws Exception {
        when(authorizationService.authenticate(any(LoginDTO.class))).thenReturn(new TokenDTO("dummy-token"));

        LoginDTO loginDTO = new LoginDTO("test@email.com", "password_hash");

        mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-token"));
    }

    @Test
    public void loginEndpoint_shouldReturnUnauthorised_whenUserNotFound() throws Exception {
        when(authorizationService.authenticate(any(LoginDTO.class))).thenThrow(new UserNotFoundException());

        LoginDTO loginDTO = new LoginDTO("test@email.com", "password_hash");

        mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginEndpoint_shouldReturnUnauthorised_whenCredentialsAreInvalid() throws Exception {
        when(authorizationService.authenticate(any(LoginDTO.class))).thenThrow(new InvalidCredentialsException());

        LoginDTO loginDTO = new LoginDTO("test@email.com", "password_hash");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }
}
