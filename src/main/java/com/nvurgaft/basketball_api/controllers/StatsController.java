package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.PlayerStats;
import com.nvurgaft.basketball_api.services.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
@Tag(name = "Statistics", description = "Statistics API")
@RequestMapping("api/v1/stats")
public class StatsController {

    private StatsService statsService;

    @Operation(summary = "Returns all player stats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Something went wrong",
                    content = @Content)})
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

    @Operation(summary = "Create a player stats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
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

    @Operation(summary = "Delete a stats records by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "304", description = "Not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStatById(
            @Valid @NotNull(message = "Stat ID cannot be null") @PathVariable(required = false) UUID id) {
        try {
            boolean removed = statsService.deleteStat(id);
            if (!removed) {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed deleting stat with id {}", id, t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Returns all stats records for a player by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @GetMapping("/player/{id}")
    public ResponseEntity<List<PlayerStats>> getPlayerStats(
            @Valid @NotNull(message = "Player ID cannot be null") @RequestParam(required = true) UUID playerId
    ) {
        try {
            List<PlayerStats> stats = statsService.getStatsByPlayerId(playerId);
            if (stats.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed fetching teams", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Return all stats records for a player for a specific season using his ID and season year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @GetMapping("/player/season/{id}")
    public ResponseEntity<List<PlayerStats>> getPlayerSeasonStats(
            @Valid @NotNull(message = "Player ID cannot be null") @RequestParam(required = true) UUID playerId,
            @Valid @Min(value = 1800, message = "Min year value is 1800") @RequestParam(required = true) int season
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

    @Operation(summary = "Return all stats records for a player for a specific team using his ID and team ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
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

    @Operation(summary = "Update player stats by stats ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @PutMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePlayerStats(@Valid @NotNull @PathVariable(required = true) PlayerStats playerStats) {
        try {
            if (Objects.isNull(playerStats.getId())) {
                log.error("Player stats was not provided");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            boolean added = statsService.updateStat(playerStats);
            if (!added) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed updating player stats", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
