package com.noom.interview.fullstack.controllers;

import com.noom.interview.fullstack.dtos.UserDTO;
import com.noom.interview.fullstack.dtos.UserRequestDTO;
import com.noom.interview.fullstack.exceptions.UserNotUniqueException;
import com.noom.interview.fullstack.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequestDTO user) throws UserNotUniqueException {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}