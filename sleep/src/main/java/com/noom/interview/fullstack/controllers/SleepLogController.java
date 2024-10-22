package com.noom.interview.fullstack.controllers;

import com.noom.interview.fullstack.dtos.SleepLogDTO;
import com.noom.interview.fullstack.dtos.SleepLogRequestDTO;
import com.noom.interview.fullstack.services.SleepLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sleep-logs")
public class SleepLogController {
    @Autowired
    private SleepLogService sleepLogService;

    @PostMapping
    public ResponseEntity<SleepLogDTO> createSleepLog(@RequestBody SleepLogRequestDTO sleepLogRequestDTO) {
        return ResponseEntity.ok(sleepLogService.createSleepLog(sleepLogRequestDTO));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SleepLogDTO>> getSleepLogsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(sleepLogService.getSleepLogsByUserId(userId));
    }

    @GetMapping("/user/{userId}/last-night")
    public ResponseEntity<List<SleepLogDTO>> getLastNightSleepLogByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(sleepLogService.getLastNightSleepLogByUserId(userId));
    }
}