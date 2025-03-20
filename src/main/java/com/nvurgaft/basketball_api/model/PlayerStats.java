package com.nvurgaft.basketball_api.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
@Data
@AllArgsConstructor
public class PlayerStats {

    private UUID id;
    private Player player;
    private Team team;
    private int season;

    private int points;
    private int rebounds;
    private int assists;
    private int steals;
    private int blocks;
    private int fouls; // max value is 6
    private int turnovers;
    private float minutesPlayed; // between 0 and 48.0
}
