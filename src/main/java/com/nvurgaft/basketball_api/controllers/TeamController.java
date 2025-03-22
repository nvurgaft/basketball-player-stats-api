package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.Team;
import com.nvurgaft.basketball_api.services.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "Teams", description = "Teams API")
@RequestMapping("api/v1/teams")
public class TeamController {

    private TeamService teamService;

    @Operation(summary = "Return all teams")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @GetMapping("/")
    public ResponseEntity<List<Team>> getAllTeams() {
        try {
            List<Team> teams = teamService.getAllTeams();
            if (teams.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(teams, HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed fetching teams", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Return team data by team ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(
            @Valid @NotNull(message = "Team ID is required") @PathVariable(required = true) UUID id) {
        try {
            Optional<Team> team = teamService.getTeamById(id);
            if (team.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(team.get(), HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed fetching team with id {}", id, t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a team by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "304", description = "Not modified",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeamById(
            @Valid @NotNull(message = "Team ID is required") @PathVariable(required = true) UUID id) {
        try {
            boolean removed = teamService.deleteTeam(id);
            if (!removed) {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed deleting team with id {}", id, t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create a new team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "304", description = "Not modified",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @PostMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addTeam(
            @Valid @NotNull(message = "Team is required") @RequestBody Team team
    ) {
        try {
            boolean added = teamService.addTeam(team);
            if (!added) {
                log.error("Failed storing team data");
                return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed adding team with id {}", team.getId(), t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update a team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "304", description = "Not modified",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @PutMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTeam(
            @Valid @NotNull(message = "Team is required") @PathVariable(required = true) Team team) {
        try {
            if (Objects.isNull(team.getId())) {
                log.error("Team ID was not provided");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            boolean modified = teamService.updateTeam(team);
            if (!modified) {
                return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed updating team", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
