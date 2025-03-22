package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.services.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("api/v1/players")
public class PlayerController {

    private PlayerService playerService;

    @Operation(summary = "Return all the players data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @GetMapping("/")
    public ResponseEntity<List<Player>> getAll() {
        try {
            List<Player> players = playerService.getAll();
            if (players.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(players, HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed fetching all players", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Return a player by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable(required = true) UUID id) {
        try {
            Optional<Player> player = playerService.getPlayerById(id);
            if (player.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(player.get(), HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed fetching all players", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a player by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "304", description = "Not modified",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeamById(
            @Valid @NotNull(message = "Player ID is required") @PathVariable(required = true) UUID playerId) {
        try {
            boolean removed = playerService.deletePlayer(playerId);
            if (removed) {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed player team with id {}", playerId, t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create a new player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "304", description = "Not modified",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @PostMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewPlayer(
            @Valid @NotNull(message = "Player is required") @PathVariable(required = true) Player player) {
        try {
            boolean added = playerService.addPlayer(player);
            if (!added) {
                return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed adding player", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update an existing player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @PutMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePlayer(
            @Valid @NotNull(message = "Player is required") @PathVariable(required = true) Player player) {
        try {
            if (Objects.isNull(player.getId())) {
                log.error("Player ID was not provided");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            boolean added = playerService.updatePlayer(player);
            if (!added) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed updating player", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
