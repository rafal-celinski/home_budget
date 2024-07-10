package xyz.celinski.home_budget.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import xyz.celinski.home_budget.dto.RegisterDTO;
import xyz.celinski.home_budget.dto.UserDTO;
import xyz.celinski.home_budget.exception.InvalidUserDetailsException;
import xyz.celinski.home_budget.exception.UserAlreadyExistsException;
import xyz.celinski.home_budget.model.User;
import xyz.celinski.home_budget.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO registerNewUser(RegisterDTO registerDTO) {
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        try {
            User user = registerDTOtoUser(registerDTO);
            user = userRepository.save(user);
            return userToUserDTO(user);
        }
        catch (DataAccessException e) {
            throw new InvalidUserDetailsException("Email and password cannot be empty");
        }
    }

    private User registerDTOtoUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPasswordHash(registerDTO.getPasswordHash());
        return user;
    }

    private UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

}
