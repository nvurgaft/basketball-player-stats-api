package com.nvurgaft.basketball_api.model;

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

    @NotNull(message = "Player cannot be null")
    private Player player;

    @NotNull(message = "Team cannot be null")
    private Team team;

    @Min(value = 1800, message = "Season should not be less than 1800")
    private int season;

    @Min(value = 0, message = "Points should not be less than 0")
    private int points;

    @Min(value = 0, message = "Rebounds should not be less than 0")
    private int rebounds;

    @Min(value = 0, message = "Assists should not be less than 0")
    private int assists;

    @Min(value = 0, message = "Steals should not be less than 0")
    private int steals;

    @Min(value = 0, message = "Blocks should not be less than 0")
    private int blocks;

    @Min(value = 0, message = "Fouls should not be less than 0")
    @Max(value = 6, message = "Fouls should be not be greater than 6")
    private int fouls;

    @Min(value = 0, message = "Turnovers should not be less than 0")
    private int turnovers;

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
