package com.noom.interview.fullstack.repositories;

import com.noom.interview.fullstack.models.SleepLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SleepLogRepository extends JpaRepository<SleepLog, Long> {
    List<SleepLog> findByUserId(Long userId);
}