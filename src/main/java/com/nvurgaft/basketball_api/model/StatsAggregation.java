package com.nvurgaft.basketball_api.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to store aggregate calculation on player game statistics such as player season average or
 * team season average
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsAggregation {

    @Min(value = 0, message = "Points should not be less than 0")
    private float points;

    @Min(value = 0, message = "Rebounds should not be less than 0")
    private float rebounds;

    @Min(value = 0, message = "Assists should not be less than 0")
    private float assists;

    @Min(value = 0, message = "Steals should not be less than 0")
    private float steals;

    @Min(value = 0, message = "Blocks should not be less than 0")
    private float blocks;

    @Min(value = 0, message = "Fouls should not be less than 0")
    @Max(value = 6, message = "Fouls should be not be greater than 6")
    private float fouls;

    @Min(value = 0, message = "Turnovers should not be less than 0")
    private float turnovers;

    @Min(value = 0, message = "Minutes played should not be less than 0")
    @Max(value = 48, message = "Minutes played should not be greater than 48")
    private float minutesPlayed;
}
