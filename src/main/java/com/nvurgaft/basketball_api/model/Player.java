package com.nvurgaft.basketball_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player implements Serializable {

    private UUID id;

    @Schema(name = "Name", example = "John", required = true)
    @NotBlank(message = "Player name cannot be blank")
    @Size(min = 1, max = 50, message = "Player name must be between 1 and 50 characters")
    private String name;

    @Schema(name = "Surname", example = "Stockton", required = true)
    @NotBlank(message = "Player surname cannot be blank")
    @Size(min = 1, max = 50, message = "Player surname must be between 1 and 50 characters")
    private String surname;

    public Player(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }
}
