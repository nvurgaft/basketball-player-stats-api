package com.nvurgaft.basketball_api.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Team {

    @NotNull(message = "Id cannot be null")
    private UUID id;

    @Size(min = 5, max = 50, message = "Team name must be between 5 and 50 characters")
    private String name;
}
