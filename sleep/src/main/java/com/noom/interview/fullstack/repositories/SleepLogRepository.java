package com.noom.interview.fullstack.repositories;

import com.noom.interview.fullstack.models.SleepLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SleepLogRepository extends JpaRepository<SleepLog, Long> {
    List<SleepLog> findByUserId(Long userId);

    @Query("SELECT sl FROM SleepLog sl WHERE sl.user.id = :userId AND sl.sleepStart >= :lastNightStart AND sl.sleepStart < :lastNightEnd")
    List<SleepLog> findLastNightSleepLogsByUserId(
            @Param("userId") Long userId,
            @Param("lastNightStart") LocalDateTime lastNightStart,
            @Param("lastNightEnd") LocalDateTime lastNightEnd);

    @Query("SELECT sl FROM SleepLog sl WHERE sl.user.id = :userId AND sl.sleepStart >= :startDate AND sl.sleepStart < :endDate")
    List<SleepLog> findByUserIdAndDateRange(@Param("userId") Long userId,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);
}