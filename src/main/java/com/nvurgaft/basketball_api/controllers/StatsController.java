package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.PlayerStats;
import com.nvurgaft.basketball_api.services.StatsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@AllArgsConstructor
@RestController
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


    @PostMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addStats(
            @Valid @NotNull(message = "Player stats cannot be null") @RequestBody PlayerStats playerStats
    ) {
        try {
            boolean added = statsService.addStat(playerStats);
            if (!added) {
                log.error("Failed storing player statistics");
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed storing player statistics", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStatById(@PathVariable(required = false) UUID id) {
        try {
            boolean removed = statsService.deleteStat(id);
            if (removed) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed deleting stat with id {}", id, t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/player/{id}")
    public ResponseEntity<List<PlayerStats>> getPlayerStats(
            @Valid @NotNull(message = "Player ID cannot be null") @RequestParam(required = true) UUID playerId
    ) {
        try {
            List<PlayerStats> stats = statsService.getStatsByPlayerId(playerId);
            if (stats.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed fetching teams", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/player/season/{id}")
    public ResponseEntity<List<PlayerStats>> getPlayerSeasonStats(
            @Valid @NotNull(message = "Player ID cannot be null") @RequestParam(required = true) UUID playerId,
            @RequestParam(required = true) int season
    ) {
        try {
            List<PlayerStats> stats = statsService.getPlayerSeasonStats(playerId, season);
            if (stats.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed fetching teams", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/player/team/{id}")
    public ResponseEntity<List<PlayerStats>> getPlayerTeamStats(
            @Valid @NotNull(message = "Player ID cannot be null") @RequestParam(required = true) UUID playerId,
            @Valid @NotNull(message = "Team ID cannot be null") @RequestParam(required = true) UUID teamId
    ) {
        try {
            List<PlayerStats> stats = statsService.getPlayerTeamStats(playerId, teamId);
            if (stats.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed fetching teams", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePlayerStatsById(@Valid @NotNull @PathVariable(required = true) PlayerStats playerStats) {
        try {
            if (Objects.isNull(playerStats.getId())) {
                log.error("Player stats was not provided");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            boolean added = statsService.updateStat(playerStats);
            if (!added) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed updating player stats", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
