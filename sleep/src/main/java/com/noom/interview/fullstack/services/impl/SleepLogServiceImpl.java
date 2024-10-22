package com.noom.interview.fullstack.services.impl;

import com.noom.interview.fullstack.dtos.SleepLogDTO;
import com.noom.interview.fullstack.exceptions.UserNotFoundException;
import com.noom.interview.fullstack.models.SleepLog;
import com.noom.interview.fullstack.models.User;
import com.noom.interview.fullstack.repositories.SleepLogRepository;
import com.noom.interview.fullstack.repositories.UserRepository;
import com.noom.interview.fullstack.services.SleepLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SleepLogServiceImpl implements SleepLogService {

    private static final Logger logger = LoggerFactory.getLogger(SleepLogServiceImpl.class);

    @Autowired
    private SleepLogRepository sleepLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<SleepLogDTO> getSleepLogsByUserId(Long userId) {
        logger.info("Fetching sleep logs for user with userId={}", userId);
        List<SleepLog> sleepLogs = sleepLogRepository.findByUserId(userId);
        logger.info("Found {} sleep logs for user with userId={}", sleepLogs.size(), userId);
        return sleepLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public SleepLogDTO createSleepLog(SleepLogDTO sleepLogDTO) {
        logger.info("Creating new sleep log for user with id={}", sleepLogDTO.getUserId());
        SleepLog sleepLog = mapDtoToEntity(sleepLogDTO);
        SleepLog savedSleepLog = sleepLogRepository.save(sleepLog);
        logger.info("Successfully created sleep log with id={} userId={}", savedSleepLog.getId(), sleepLogDTO.getUserId());
        return convertToDTO(savedSleepLog);
    }

    private SleepLog mapDtoToEntity(SleepLogDTO dto) {
        logger.debug("Mapping SleepLogDTO={} to SleepLog entity", dto);
        SleepLog sleepLog = new SleepLog();
        sleepLog.setId(dto.getId());

        User user = getUserById(dto.getUserId());
        sleepLog.setUser(user);
        sleepLog.setSleepStart(dto.getSleepStart());
        sleepLog.setSleepEnd(dto.getSleepEnd());
        sleepLog.setMood(dto.getMood());
        logger.debug("Mapped SleepLogDTO to sleepLog={}", sleepLog);
        return sleepLog;
    }

    private User getUserById(Long userId) {
        logger.debug("Fetching User for userId={}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with id={} not found", userId);
                    return new UserNotFoundException(String.format("User not found with id=%s", userId));
                });
    }

    private SleepLogDTO convertToDTO(SleepLog sleepLog) {
        logger.debug("Converting SleepLog={} entity to SleepLogDTO", sleepLog);
        SleepLogDTO dto = SleepLogDTO.builder()
                .id(sleepLog.getId())
                .userId(sleepLog.getUser().getId())
                .sleepStart(sleepLog.getSleepStart())
                .sleepEnd(sleepLog.getSleepEnd())
                .mood(sleepLog.getMood())
                .build();
        logger.debug("Converted SleepLog entity to SleepLogDTO={}", dto);
        return dto;
    }
}
