package com.noom.interview.fullstack.services.impl;

import com.noom.interview.fullstack.dtos.UserDTO;
import com.noom.interview.fullstack.models.User;
import com.noom.interview.fullstack.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder()
                .id(1L)
                .username("test_user")
                .email("test_user@example.com")
                .build();
    }

    @Test
    void createUser_ShouldSaveAndReturnUserDTO() {
        User user = new User();
        user.setId(1L);
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO savedUserDTO = userService.createUser(userDTO);

        assertEquals(userDTO.getUsername(), savedUserDTO.getUsername());
        assertEquals(userDTO.getEmail(), savedUserDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getAllUsers_ShouldReturnUserDTOList() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserDTO> userDTOs = userService.getAllUsers();

        assertEquals(2, userDTOs.size());
        assertEquals(user1.getUsername(), userDTOs.get(0).getUsername());
        assertEquals(user2.getUsername(), userDTOs.get(1).getUsername());
        verify(userRepository, times(1)).findAll();
    }
}
