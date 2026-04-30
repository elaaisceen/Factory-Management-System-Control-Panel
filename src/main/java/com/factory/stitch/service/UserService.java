package com.factory.stitch.service;

import com.factory.stitch.model.User;
import com.factory.stitch.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean createUser(String username, String password, String role) {
        if (userRepository.existsByUsername(username)) {
            return false;
        }
        userRepository.save(new User(username, password, role));
        return true;
    }

    public boolean authenticateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .map(u -> u.getPassword().equals(password))
                .orElse(false);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
}