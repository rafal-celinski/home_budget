package xyz.celinski.home_budget.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import xyz.celinski.home_budget.exception.InvalidUserDetailsException;
import xyz.celinski.home_budget.exception.UserAlreadyExistsException;
import xyz.celinski.home_budget.model.User;
import xyz.celinski.home_budget.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerNewUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        try {
            return userRepository.save(user);
        }
        catch (DataAccessException e) {
            throw new InvalidUserDetailsException("Email and password cannot be empty");
        }
    }

}
