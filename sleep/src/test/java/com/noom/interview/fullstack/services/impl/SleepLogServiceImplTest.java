package com.noom.interview.fullstack.services.impl;

import com.noom.interview.fullstack.dtos.SleepLogDTO;
import com.noom.interview.fullstack.exceptions.UserNotFoundException;
import com.noom.interview.fullstack.models.SleepLog;
import com.noom.interview.fullstack.models.User;
import com.noom.interview.fullstack.repositories.SleepLogRepository;
import com.noom.interview.fullstack.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SleepLogServiceImplTest {

    @Mock
    private SleepLogRepository sleepLogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SleepLogServiceImpl sleepLogService;

    private User testUser;
    private SleepLog testSleepLog;
    private SleepLogDTO testSleepLogDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");

        testSleepLog = new SleepLog();
        testSleepLog.setId(1L);
        testSleepLog.setUser(testUser);
        testSleepLog.setSleepStart(LocalDateTime.now().minusHours(8));
        testSleepLog.setSleepEnd(LocalDateTime.now());
        testSleepLog.setMood("Happy");

        testSleepLogDTO = SleepLogDTO.builder()
                .id(testSleepLog.getId())
                .userId(testUser.getId())
                .sleepStart(testSleepLog.getSleepStart())
                .sleepEnd(testSleepLog.getSleepEnd())
                .mood(testSleepLog.getMood())
                .build();
    }

    @Test
    void testGetSleepLogsByUserId() {
        when(sleepLogRepository.findByUserId(testUser.getId())).thenReturn(Arrays.asList(testSleepLog));

        var result = sleepLogService.getSleepLogsByUserId(testUser.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSleepLogDTO, result.get(0));
        verify(sleepLogRepository, times(1)).findByUserId(testUser.getId());
    }

    @Test
    void testCreateSleepLog() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(sleepLogRepository.save(any(SleepLog.class))).thenReturn(testSleepLog);

        var result = sleepLogService.createSleepLog(testSleepLogDTO);

        assertNotNull(result);
        assertEquals(testSleepLogDTO.getId(), result.getId());
        verify(userRepository, times(1)).findById(testUser.getId());
        verify(sleepLogRepository, times(1)).save(any(SleepLog.class));
    }

    @Test
    void testCreateSleepLog_UserNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> sleepLogService.createSleepLog(testSleepLogDTO));
        verify(userRepository, times(1)).findById(testUser.getId());
        verify(sleepLogRepository, never()).save(any(SleepLog.class));
    }
}
