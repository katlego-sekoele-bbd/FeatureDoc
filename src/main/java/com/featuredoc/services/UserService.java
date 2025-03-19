package com.featuredoc.services;

import com.featuredoc.models.User;
import com.featuredoc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {

        return userRepository.findById(id);
    }


    public User createUser(User user) {

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }

    public Optional<User> getUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof String) {

            return getUserByEmail(String.valueOf(authentication.getPrincipal())).get();
        }
        throw new IllegalStateException("User not found");
    }
}
