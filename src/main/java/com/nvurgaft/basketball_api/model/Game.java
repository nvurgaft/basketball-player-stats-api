package com.nvurgaft.basketball_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.time.ZonedDateTime;
import java.util.UUID;

@Log4j2
@Data
@AllArgsConstructor
public class Game {

    private UUID id;
    private Team homeTeam;
    private Team awayTeam;
    private int season;
    private ZonedDateTime playedAt;
}
