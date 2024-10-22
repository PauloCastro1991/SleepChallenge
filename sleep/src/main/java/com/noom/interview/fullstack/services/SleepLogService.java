package com.noom.interview.fullstack.services;

import com.noom.interview.fullstack.dtos.SleepLogDTO;
import com.noom.interview.fullstack.dtos.SleepLogRequestDTO;

import java.util.List;

public interface SleepLogService {
    List<SleepLogDTO> getSleepLogsByUserId(Long userId);

    SleepLogDTO createSleepLog(SleepLogRequestDTO sleepLogRequestDTO);

    List<SleepLogDTO> getLastNightSleepLogByUserId(Long userId);
}
