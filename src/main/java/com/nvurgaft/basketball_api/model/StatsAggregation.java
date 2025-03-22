package com.nvurgaft.basketball_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * This class is used to store aggregate calculation on player game statistics such as player season average or
 * team season average
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsAggregation implements Serializable {

    @Schema(name = "Points made", example = "32.5", required = true)
    @Min(value = 0, message = "Points should not be less than 0")
    private float points;

    @Schema(name = "Rebounds made", example = "8.0", required = true)
    @Min(value = 0, message = "Rebounds should not be less than 0")
    private float rebounds;

    @Schema(name = "Assists", example = "13.5", required = true)
    @Min(value = 0, message = "Assists should not be less than 0")
    private float assists;

    @Schema(name = "Steals", example = "17.0", required = true)
    @Min(value = 0, message = "Steals should not be less than 0")
    private float steals;

    @Schema(name = "Blocks", example = "12.5", required = true)
    @Min(value = 0, message = "Blocks should not be less than 0")
    private float blocks;

    @Schema(name = "Fouls committed", example = "2.0", required = true)
    @Min(value = 0, message = "Fouls should not be less than 0")
    @Max(value = 6, message = "Fouls should be not be greater than 6")
    private float fouls;

    @Schema(name = "Turnovers", example = "5.5", required = true)
    @Min(value = 0, message = "Turnovers should not be less than 0")
    private float turnovers;

    @Schema(name = "Minutes played in game", example = "32.5", required = true)
    @Min(value = 0, message = "Minutes played should not be less than 0")
    @Max(value = 48, message = "Minutes played should not be greater than 48")
    private float minutesPlayed;
}
