package com.noom.interview.fullstack.dtos;

import com.noom.interview.fullstack.enums.Mood;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@Builder
public class SleepLogRequestDTO {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Sleep start time cannot be null")
    private LocalTime sleepStart;

    @NotNull(message = "Sleep end time cannot be null")
    private LocalTime sleepEnd;

    @NotNull(message = "Mood cannot be null")
    private Mood mood;
}
