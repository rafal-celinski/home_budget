package pl.rafalcelinski.home_budget.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pl.rafalcelinski.home_budget.dto.RegisterDTO;
import pl.rafalcelinski.home_budget.dto.UserDTO;
import pl.rafalcelinski.home_budget.exception.InvalidUserDetailsException;
import pl.rafalcelinski.home_budget.exception.UserAlreadyExistsException;
import pl.rafalcelinski.home_budget.entity.User;
import pl.rafalcelinski.home_budget.mapper.UserMapper;
import pl.rafalcelinski.home_budget.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO registerNewUser(RegisterDTO registerDTO) {
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        try {
            User user = userMapper.toEntity(registerDTO);
            user = userRepository.save(user);
            return userMapper.toDTO(user);
        }
        catch (DataAccessException e) {
            throw new InvalidUserDetailsException("Email and password cannot be empty");
        }
    }
}
