package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.StatsAggregation;
import com.nvurgaft.basketball_api.services.AggregationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/aggs")
public class StatAggregationController {

    private AggregationsService aggregationsService;

    @Operation(summary = "Return the season averages for a player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @GetMapping("/player/average")
    public ResponseEntity<StatsAggregation> getPlayerSeasonAverage(
            @Valid @NotNull(message = "Player ID is required") @RequestParam(required = true) UUID playerId,
            @Valid @Min(value = 1800, message = "Min year value is 1800") @RequestParam(required = true) int season
    ) {
        try {
            Optional<StatsAggregation> aggregation = aggregationsService.getPlayerSeasonAverage(playerId, season);
            if (aggregation.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(aggregation.get(), HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed fetching teams", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Return the season averages for a team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @GetMapping("/team/average")
    public ResponseEntity<StatsAggregation> getTeamSeasonAverage(
            @Valid @NotNull(message = "Team ID is required") @RequestParam(required = true) UUID teamId,
            @Valid @Min(value = 1800, message = "Min year value is 1800") @RequestParam(required = true) int season) {
        try {
            Optional<StatsAggregation> aggregation = aggregationsService.getTeamSeasonAverage(teamId, season);
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
