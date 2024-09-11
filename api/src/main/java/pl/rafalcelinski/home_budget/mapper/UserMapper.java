package pl.rafalcelinski.home_budget.mapper;

import org.springframework.stereotype.Component;
import pl.rafalcelinski.home_budget.dto.RegisterDTO;
import pl.rafalcelinski.home_budget.dto.UserDTO;
import pl.rafalcelinski.home_budget.entity.User;

@Component
public class UserMapper {
    public User toEntity(RegisterDTO registerDTO) {
        if (registerDTO == null) {
            return null;
        }

        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPasswordHash(registerDTO.getPasswordHash());

        return user;
    }

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());

        return userDTO;
    }
}
