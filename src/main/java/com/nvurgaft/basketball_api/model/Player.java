package com.nvurgaft.basketball_api.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @NotNull(message = "Id cannot be null")
    private UUID id;

    @Size(min = 1, max = 50, message = "Player name must be between 1 and 50 characters")
    private String name;

    @Size(min = 1, max = 50, message = "Player surname must be between 1 and 50 characters")
    private String surname;
}
