package com.noom.interview.fullstack.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SleepLogDTO {
    private Long id;
    private Long userId;
    private LocalDateTime sleepStart;
    private LocalDateTime sleepEnd;
    private String mood;
}