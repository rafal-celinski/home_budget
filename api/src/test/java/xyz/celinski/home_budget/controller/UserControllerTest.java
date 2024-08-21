package xyz.celinski.home_budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.celinski.home_budget.dto.RegisterDTO;
import xyz.celinski.home_budget.dto.UserDTO;
import xyz.celinski.home_budget.exception.InvalidUserDetailsException;
import xyz.celinski.home_budget.exception.UserAlreadyExistsException;
import xyz.celinski.home_budget.service.UserService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void registerEndpoint_shouldReturnUserDTO_whenUserWasAdded() throws Exception {
        when(userService.registerNewUser(any(RegisterDTO.class))).thenReturn(new UserDTO("test@email.com"));

        RegisterDTO registerDTO = new RegisterDTO("test@email.com", "passwordHash");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@email.com"));
    }

    @Test
    public void registerEndpoint_shouldReturnConflict_whenUserAlreadyExists() throws Exception {
        when(userService.registerNewUser(any(RegisterDTO.class))).thenThrow(new UserAlreadyExistsException());

        RegisterDTO registerDTO = new RegisterDTO("test@email.com", "passwordHash");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    public void registerEndpoint_shouldReturnBadRequest_whenRegisterDetailsAreInvalid() throws Exception {
        when(userService.registerNewUser(any(RegisterDTO.class))).thenThrow(new InvalidUserDetailsException());

        RegisterDTO registerDTO = new RegisterDTO("test@email.com", "passwordHash");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }
}
