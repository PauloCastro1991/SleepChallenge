package com.noom.interview.fullstack.services.impl;

import com.noom.interview.fullstack.dtos.UserDTO;
import com.noom.interview.fullstack.dtos.UserRequestDTO;
import com.noom.interview.fullstack.exceptions.UserNotUniqueException;
import com.noom.interview.fullstack.models.User;
import com.noom.interview.fullstack.repositories.UserRepository;
import com.noom.interview.fullstack.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO createUser(UserRequestDTO userDTO) throws UserNotUniqueException {
        logger.info("Creating new user - username={}", userDTO.getUsername());
        User user = mapDtoToEntity(userDTO);
        validateUser(user);
        User savedUser = userRepository.save(user);
        logger.info("Successfully created user with userId={}", savedUser.getId());
        return mapEntityToDto(savedUser);
    }

    private void validateUser(User user) throws UserNotUniqueException {
        if (userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail()).isPresent()) {
            String msg = String.format("The username=%s and email=%s must be unique", user.getUsername(), user.getEmail());
            logger.warn(msg);
            throw new UserNotUniqueException(msg);
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userRepository.findAll();
        logger.info("Found {} users", users.size());
        return users.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    private User mapDtoToEntity(UserRequestDTO dto) {
        logger.debug("Mapping UserDTO to User entity: {}", dto);
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        logger.debug("Mapped UserDTO to entity: {}", user);
        return user;
    }

    private UserDTO mapEntityToDto(User user) {
        logger.debug("Mapping User={} to UserDTO", user);
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
