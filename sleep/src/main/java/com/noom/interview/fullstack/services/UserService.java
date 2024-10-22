package com.noom.interview.fullstack.services;

import com.noom.interview.fullstack.dtos.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();
}
