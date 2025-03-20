package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.PlayerStats;
import com.nvurgaft.basketball_api.model.StatsAggregation;
import com.nvurgaft.basketball_api.services.StatsService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("api/v1/stats")
public class StatsController {

    private StatsService statsService;

    @GetMapping("/")
    public ResponseEntity<List<PlayerStats>> getAllStats() {
        try {
            List<PlayerStats> stats = statsService.getAllStats();
            if (stats.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed fetching teams", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/player/average")
    public ResponseEntity<StatsAggregation> getPlayerSeasonAverage(
            @RequestParam(required = true) UUID playerId,
            @RequestParam(required = true) int season
    ) {
        try {
            Optional<StatsAggregation> aggregation = statsService.getPlayerSeasonAverage(playerId, season);
            if (aggregation.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(aggregation.get(), HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed fetching teams", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/team/average")
    public ResponseEntity<StatsAggregation> getTeamSeasonAverage(
            @RequestParam(required = true) UUID teamId,
            @RequestParam(required = true) int season) {
        try {
            Optional<StatsAggregation> aggregation = statsService.getTeamSeasonAverage(teamId, season);
            if (aggregation.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(aggregation.get(), HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed fetching player's season average with id {}", teamId, t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
