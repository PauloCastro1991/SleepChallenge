package com.noom.interview.fullstack.dtos;

import com.noom.interview.fullstack.enums.Mood;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class SleepLogRequestDTO {
    private Long userId;
    private LocalTime sleepStart;
    private LocalTime sleepEnd;
    private Mood mood;
}
