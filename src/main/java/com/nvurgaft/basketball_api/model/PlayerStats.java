package com.nvurgaft.basketball_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStats implements Serializable {

    private UUID id;

    @Schema(name = "Player", required = true)
    @NotNull(message = "Player cannot be null")
    private Player player;

    @Schema(name = "Team", required = true)
    @NotNull(message = "Team cannot be null")
    private Team team;

    @Schema(name = "Season year", example = "2007", required = true)
    @Min(value = 1800, message = "Season should not be less than 1800")
    private int season;

    @Schema(name = "Points made", example = "32", required = true)
    @Min(value = 0, message = "Points should not be less than 0")
    private int points;

    @Schema(name = "Rebounds made", example = "8", required = true)
    @Min(value = 0, message = "Rebounds should not be less than 0")
    private int rebounds;

    @Schema(name = "Assists", example = "13", required = true)
    @Min(value = 0, message = "Assists should not be less than 0")
    private int assists;

    @Schema(name = "Steals", example = "17", required = true)
    @Min(value = 0, message = "Steals should not be less than 0")
    private int steals;

    @Schema(name = "Blocks", example = "12", required = true)
    @Min(value = 0, message = "Blocks should not be less than 0")
    private int blocks;

    @Schema(name = "Fouls committed", example = "2", required = true)
    @Min(value = 0, message = "Fouls should not be less than 0")
    @Max(value = 6, message = "Fouls should be not be greater than 6")
    private int fouls;

    @Schema(name = "Turnovers", example = "5", required = true)
    @Min(value = 0, message = "Turnovers should not be less than 0")
    private int turnovers;

    @Schema(name = "Minutes played in game", example = "42", required = true)
    @Min(value = 0, message = "Minutes played should not be less than 0")
    @Max(value = 48, message = "Minutes played should not be greater than 48")
    private float minutesPlayed;

    public PlayerStats(Player player, Team team, int season, int points, int rebounds, int assists, int steals, int blocks, int fouls, int turnovers, float minutesPlayed) {
        this.player = player;
        this.team = team;
        this.season = season;
        this.points = points;
        this.rebounds = rebounds;
        this.assists = assists;
        this.steals = steals;
        this.blocks = blocks;
        this.fouls = fouls;
        this.turnovers = turnovers;
        this.minutesPlayed = minutesPlayed;
    }
}
