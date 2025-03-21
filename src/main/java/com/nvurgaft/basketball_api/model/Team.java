package com.nvurgaft.basketball_api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team implements Serializable {

    private UUID id;

    @NotBlank(message = "Team name cannot by blank")
    @Size(min = 5, max = 50, message = "Team name must be between 5 and 50 characters")
    private String name;

    public Team(String name) {
        this.name = name;
    }
}
