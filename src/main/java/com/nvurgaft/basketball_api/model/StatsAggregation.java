package com.nvurgaft.basketball_api.model;

import java.util.UUID;

/**
 * This class is used to store aggregate calculation on player game statistics such as player season average or
 * team season average
 */
public class StatsAggregation {
    private UUID id;
    private AggregationType type;

    private float points;
    private float rebounds;
    private float assists;
    private float steals;
    private float blocks;
    private float fouls; // max value is 6
    private float turnovers;
    private float minutesPlayed; // between 0 and 48.0
}
