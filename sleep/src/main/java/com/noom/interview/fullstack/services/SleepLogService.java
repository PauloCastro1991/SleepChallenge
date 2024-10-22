package com.noom.interview.fullstack.services;

import com.noom.interview.fullstack.dtos.SleepLogDTO;

import java.util.List;

public interface SleepLogService {
    List<SleepLogDTO> getSleepLogsByUserId(Long userId);

    SleepLogDTO createSleepLog(SleepLogDTO sleepLogDTO);
}
