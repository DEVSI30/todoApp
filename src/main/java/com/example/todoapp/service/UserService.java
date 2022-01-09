package com.example.todoapp.service;

import com.example.todoapp.model.UserEntity;
import com.example.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if (userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }

        final String email = userEntity.getEmail();

        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String email, final String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}
