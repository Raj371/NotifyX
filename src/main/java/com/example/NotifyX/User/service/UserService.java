package com.example.NotifyX.User.service;

import com.example.NotifyX.User.dto.CreateUserRequest;
import com.example.NotifyX.User.dto.UserResponse;
import com.example.NotifyX.User.entity.User;
import com.example.NotifyX.User.exception.UserAlreadyExistsException;
import com.example.NotifyX.User.exception.UserNotFoundException;
import com.example.NotifyX.User.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        String normalizedEmail =
                request.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException(normalizedEmail);
        }

        User user = new User(
                request.name().trim(),
                normalizedEmail,
                request.phoneNumber()
        );

        try {
            User savedUser = userRepository.saveAndFlush(user);
            return toResponse(savedUser);
        } catch (DataIntegrityViolationException exception) {
            // Handles concurrent requests attempting the same email.
            throw new UserAlreadyExistsException(normalizedEmail);
        }
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCreatedAt()
        );
    }
}