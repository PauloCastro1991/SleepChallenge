package com.noom.interview.fullstack.services;

import com.noom.interview.fullstack.dtos.UserDTO;
import com.noom.interview.fullstack.dtos.UserRequestDTO;
import com.noom.interview.fullstack.exceptions.UserNotUniqueException;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserRequestDTO userDTO) throws UserNotUniqueException;

    List<UserDTO> getAllUsers();
}
