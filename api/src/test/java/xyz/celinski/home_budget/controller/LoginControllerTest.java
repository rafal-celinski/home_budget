package xyz.celinski.home_budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.celinski.home_budget.dto.LoginDTO;
import xyz.celinski.home_budget.exception.InvalidCredentialsException;
import xyz.celinski.home_budget.exception.UserNotFoundException;
import xyz.celinski.home_budget.service.AuthService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void loginEndpoint_shouldReturnToken_whenCredentialsAreValid() throws Exception {
        when(authService.login(anyString(), anyString())).thenReturn("dummy-token");

        LoginDTO loginDTO = new LoginDTO("test@email.com", "password_hash");

        mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-token"));
    }

    @Test
    public void loginEndpoint_shouldReturnUnauthorised_whenUserNotFound() throws Exception {
        when(authService.login(anyString(), anyString())).thenThrow(new UserNotFoundException());

        LoginDTO loginDTO = new LoginDTO("test@email.com", "password_hash");

        mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginEndpoint_shouldReturnUnauthorised_whenCredentialsAreInvalid() throws Exception {
        when(authService.login(anyString(), anyString())).thenThrow(new InvalidCredentialsException());

        LoginDTO loginDTO = new LoginDTO("test@email.com", "password_hash");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }
}
