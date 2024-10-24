package com.noom.interview.fullstack.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserRequestDTO {
    @NotNull(message = "The username cannot be null")
    private String username;
    @NotNull(message = "The email cannot be null")
    private String email;

}
