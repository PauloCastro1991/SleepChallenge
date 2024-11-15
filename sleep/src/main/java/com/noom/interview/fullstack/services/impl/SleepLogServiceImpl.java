package com.noom.interview.fullstack.services.impl;

import com.noom.interview.fullstack.dtos.SleepLogAveragesDTO;
import com.noom.interview.fullstack.dtos.SleepLogDTO;
import com.noom.interview.fullstack.dtos.SleepLogRequestDTO;
import com.noom.interview.fullstack.exceptions.NoSleepLogFoundException;
import com.noom.interview.fullstack.exceptions.SleepHoursDuplicatedException;
import com.noom.interview.fullstack.exceptions.UserNotFoundException;
import com.noom.interview.fullstack.models.SleepLog;
import com.noom.interview.fullstack.models.User;
import com.noom.interview.fullstack.repositories.SleepLogRepository;
import com.noom.interview.fullstack.repositories.UserRepository;
import com.noom.interview.fullstack.services.SleepLogService;
import com.noom.interview.fullstack.utils.CalculationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public SleepLogAveragesDTO getLast30DaysAveragesByUserId(Long userId) {
        logger.info("Fetching last 30 days sleep log averages for userId={}", userId);

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(30);

        List<SleepLog> sleepLogs = sleepLogRepository.findByUserIdAndDateRange(userId, startDate, endDate);

        return calculateAverages(sleepLogs, startDate, endDate, userId);
    }

    private SleepLogAveragesDTO calculateAverages(List<SleepLog> sleepLogs, LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        sleepLogs = sleepLogs.stream()
                .filter(log -> log.getSleepStart().isBefore(log.getSleepEnd()))
                .collect(Collectors.toList());

        return SleepLogAveragesDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .averageTotalTimeInBed(CalculationUtil.calculateAverageSleepHours(sleepLogs))
                .averageBedtime(CalculationUtil.calculateAverageTime(sleepLogs.stream().map(SleepLog::getSleepStart).collect(Collectors.toList()), CalculationUtil.TimeType.BEDTIME))
                .averageWakeTime(CalculationUtil.calculateAverageTime(sleepLogs.stream().map(SleepLog::getSleepEnd).collect(Collectors.toList()), CalculationUtil.TimeType.WAKEUP))
                .moodFrequencies(CalculationUtil.calculateMoodFrequency(sleepLogs))
                .build();
    }

    @Override
    public List<SleepLogDTO> getSleepLogsByUserId(Long userId) {
        logger.info("Fetching sleep logs for user with userId={}", userId);
        List<SleepLog> sleepLogs = sleepLogRepository.findByUserId(userId);
        logger.info("Found {} sleep logs for user with userId={}", sleepLogs.size(), userId);
        return sleepLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public SleepLogDTO createSleepLog(SleepLogRequestDTO sleepLogRequestDTO) {
        logger.info("Creating new sleep log for user with id={}", sleepLogRequestDTO.getUserId());
        SleepLog sleepLog = mapDtoToEntity(sleepLogRequestDTO);
        validateSleepLog(sleepLog);
        SleepLog savedSleepLog = sleepLogRepository.save(sleepLog);
        logger.info("Successfully created sleep log with id={} userId={}", savedSleepLog.getId(), sleepLogRequestDTO.getUserId());
        return convertToDTO(savedSleepLog);
    }

    private void validateSleepLog(SleepLog sleepLog) {
        boolean unique = sleepLogRepository.findOverlappingSleepLogs(
                sleepLog.getUser().getId(),
                sleepLog.getSleepStart(),
                sleepLog.getSleepEnd()).isEmpty();
        if (!unique) {
            String msg = String.format("The sleep hours were already registered in this range - userId=%s, start=%s, end=%s",
                    sleepLog.getUser().getId(),
                    sleepLog.getSleepStart(),
                    sleepLog.getSleepEnd());
            logger.warn(msg);
            throw new SleepHoursDuplicatedException(msg);
        }
    }

    @Override
    public List<SleepLogDTO> getLastNightSleepLogByUserId(Long userId) {
        logger.info("Fetching last night's sleep log for user with userId={}", userId);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastNightStart = now.minusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime lastNightEnd = now.toLocalDate().atStartOfDay();

        List<SleepLog> sleepLogs = sleepLogRepository.findLastNightSleepLogsByUserId(userId, lastNightStart, lastNightEnd);

        if (sleepLogs.isEmpty()) {
            logger.error("No sleep log found for user with userId={} last night", userId);
            throw new NoSleepLogFoundException("No sleep log found for last night");
        }

        return sleepLogs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SleepLog mapDtoToEntity(SleepLogRequestDTO sleepLogRequestDTO) {
        logger.debug("Mapping SleepLogRequestDTO={} to SleepLog entity", sleepLogRequestDTO);
        SleepLog sleepLog = new SleepLog();

        User user = getUserById(sleepLogRequestDTO.getUserId());
        sleepLog.setUser(user);

        // Get the current date and set the sleep start and end times to the last day's date
        LocalDate lastNightDate = LocalDate.now().minusDays(1);

        LocalTime sleepStartTime = sleepLogRequestDTO.getSleepStart();
        LocalTime sleepEndTime = sleepLogRequestDTO.getSleepEnd();

        // Combine the last night's date with the LocalTime values
        LocalDateTime sleepStartDateTime = lastNightDate.atTime(sleepStartTime);

        // Determine the date for sleepEnd: if sleepEnd is before sleepStart, it means it goes to the next day
        LocalDateTime sleepEndDateTime;
        if (sleepEndTime.isBefore(sleepStartTime)) {
            // The end time is on the next day
            sleepEndDateTime = lastNightDate.plusDays(1).atTime(sleepEndTime);
        } else {
            // The end time is on the same day
            sleepEndDateTime = lastNightDate.atTime(sleepEndTime);
        }

        // Set the sleep start and end times in the SleepLog entity
        sleepLog.setSleepStart(sleepStartDateTime);
        sleepLog.setSleepEnd(sleepEndDateTime);

        sleepLog.setMood(sleepLogRequestDTO.getMood());

        logger.debug("Mapped SleepLogRequestDTO to sleepLog={}", sleepLog);
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
