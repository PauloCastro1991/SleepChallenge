package com.noom.interview.fullstack.services.impl;

import com.noom.interview.fullstack.dtos.SleepLogDTO;
import com.noom.interview.fullstack.dtos.SleepLogRequestDTO;
import com.noom.interview.fullstack.enums.Mood;
import com.noom.interview.fullstack.exceptions.NoSleepLogFoundException;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    private SleepLogRequestDTO testSleepLogRequestDTO;

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
        testSleepLog.setMood(Mood.OK);

        testSleepLogDTO = SleepLogDTO.builder()
                .id(testSleepLog.getId())
                .userId(testUser.getId())
                .sleepStart(testSleepLog.getSleepStart())
                .sleepEnd(testSleepLog.getSleepEnd())
                .mood(testSleepLog.getMood())
                .build();

        testSleepLogRequestDTO = SleepLogRequestDTO.builder()
                .userId(testUser.getId())
                .sleepStart(testSleepLog.getSleepStart().toLocalTime())
                .sleepEnd(testSleepLog.getSleepEnd().toLocalTime())
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

        var result = sleepLogService.createSleepLog(testSleepLogRequestDTO);

        assertNotNull(result);
        assertEquals(testSleepLogRequestDTO.getUserId(), result.getUserId());
        verify(userRepository, times(1)).findById(testUser.getId());
        verify(sleepLogRepository, times(1)).save(any(SleepLog.class));
    }

    @Test
    void testCreateSleepLog_UserNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> sleepLogService.createSleepLog(testSleepLogRequestDTO));
        verify(userRepository, times(1)).findById(testUser.getId());
        verify(sleepLogRepository, never()).save(any(SleepLog.class));
    }

    @Test
    public void testGetLastNightSleepLogByUserId_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        LocalDateTime sleepStart = LocalDateTime.now().minusDays(1).withHour(22);
        LocalDateTime sleepEnd = LocalDateTime.now().minusDays(1).withHour(6);
        SleepLog sleepLog1 = new SleepLog(1L, user, sleepStart, sleepEnd, Mood.OK, LocalDateTime.now());
        SleepLog sleepLog2 = new SleepLog(2L, user, sleepStart.plusHours(1), sleepEnd.plusHours(1), Mood.BAD, LocalDateTime.now());

        when(sleepLogRepository.findLastNightSleepLogsByUserId(any(), any(), any()))
                .thenReturn(Arrays.asList(sleepLog1, sleepLog2));

        List<SleepLogDTO> result = sleepLogService.getLastNightSleepLogByUserId(userId);

        assertEquals(2, result.size());
        assertEquals(Mood.OK, result.get(0).getMood());
        assertEquals(Mood.BAD, result.get(1).getMood());
        verify(sleepLogRepository).findLastNightSleepLogsByUserId(eq(userId), any(), any());
    }

    @Test
    public void testGetLastNightSleepLogByUserId_Success_Overnight() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        // Sleep log from 10 PM yesterday to 6 AM today
        LocalDateTime sleepStart = LocalDateTime.now().minusDays(1).withHour(22);
        LocalDateTime sleepEnd = LocalDateTime.now().withHour(6).withMinute(0).withSecond(0).withNano(0);
        SleepLog sleepLog1 = new SleepLog(1L, user, sleepStart, sleepEnd, Mood.OK, LocalDateTime.now());

        // Sleep log from 11 PM yesterday to 7 AM today
        LocalDateTime sleepStart2 = LocalDateTime.now().minusDays(1).withHour(23);
        LocalDateTime sleepEnd2 = LocalDateTime.now().withHour(7).withMinute(0).withSecond(0).withNano(0);
        SleepLog sleepLog2 = new SleepLog(2L, user, sleepStart2, sleepEnd2, Mood.BAD, LocalDateTime.now());

        when(sleepLogRepository.findLastNightSleepLogsByUserId(any(), any(), any()))
                .thenReturn(Arrays.asList(sleepLog1, sleepLog2));

        List<SleepLogDTO> result = sleepLogService.getLastNightSleepLogByUserId(userId);

        assertEquals(2, result.size());
        assertEquals(Mood.OK, result.get(0).getMood());
        assertEquals(Mood.BAD, result.get(1).getMood());
        verify(sleepLogRepository).findLastNightSleepLogsByUserId(eq(userId), any(), any());
    }

    @Test
    public void testGetLastNightSleepLogByUserId_Success_SameDaySleep() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        // Sleep log from 10 PM yesterday to 11 PM yesterday (same day)
        LocalDateTime sleepStart = LocalDateTime.now().minusDays(1).withHour(22);
        LocalDateTime sleepEnd = LocalDateTime.now().minusDays(1).withHour(23);
        SleepLog sleepLog1 = new SleepLog(1L, user, sleepStart, sleepEnd, Mood.OK, LocalDateTime.now());

        // Sleep log from 9 PM yesterday to 10 PM yesterday (same day)
        LocalDateTime sleepStart2 = LocalDateTime.now().minusDays(1).withHour(21);
        LocalDateTime sleepEnd2 = LocalDateTime.now().minusDays(1).withHour(22);
        SleepLog sleepLog2 = new SleepLog(2L, user, sleepStart2, sleepEnd2, Mood.BAD, LocalDateTime.now());

        when(sleepLogRepository.findLastNightSleepLogsByUserId(any(), any(), any()))
                .thenReturn(Arrays.asList(sleepLog1, sleepLog2));

        List<SleepLogDTO> result = sleepLogService.getLastNightSleepLogByUserId(userId);

        assertEquals(2, result.size());
        assertEquals(Mood.OK, result.get(0).getMood());
        assertEquals(Mood.BAD, result.get(1).getMood());
        verify(sleepLogRepository).findLastNightSleepLogsByUserId(eq(userId), any(), any());
    }

    @Test
    public void testGetLastNightSleepLogByUserId_NoLogsFound() {
        Long userId = 1L;

        when(sleepLogRepository.findLastNightSleepLogsByUserId(eq(userId), any(), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(NoSleepLogFoundException.class, () -> {
            sleepLogService.getLastNightSleepLogByUserId(userId);
        });
        verify(sleepLogRepository).findLastNightSleepLogsByUserId(eq(userId), any(), any());
    }

    @Test
    public void testGetLastNightSleepLogByUserId_MixedLogs() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        // Sleep log from 10 PM yesterday to 6 AM today (overnight)
        LocalDateTime sleepStart1 = LocalDateTime.now().minusDays(1).withHour(22);
        LocalDateTime sleepEnd1 = LocalDateTime.now().withHour(6).withMinute(0).withSecond(0).withNano(0);
        SleepLog sleepLog1 = new SleepLog(1L, user, sleepStart1, sleepEnd1, Mood.OK, LocalDateTime.now());

        // Sleep log from 10 PM today to 11 PM today (same day)
        LocalDateTime sleepStart2 = LocalDateTime.now().withHour(22);
        LocalDateTime sleepEnd2 = LocalDateTime.now().withHour(23);
        SleepLog sleepLog2 = new SleepLog(2L, user, sleepStart2, sleepEnd2, Mood.BAD, LocalDateTime.now());

        when(sleepLogRepository.findLastNightSleepLogsByUserId(any(), any(), any()))
                .thenReturn(Arrays.asList(sleepLog1, sleepLog2));

        List<SleepLogDTO> result = sleepLogService.getLastNightSleepLogByUserId(userId);

        assertEquals(2, result.size());
        assertEquals(Mood.OK, result.get(0).getMood());
        assertEquals(Mood.BAD, result.get(1).getMood());
        verify(sleepLogRepository).findLastNightSleepLogsByUserId(eq(userId), any(), any());
    }
}
