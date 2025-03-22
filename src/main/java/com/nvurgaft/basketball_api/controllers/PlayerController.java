package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.services.PlayerService;
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

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable(required = true) UUID id) {
        try {
            Optional<Player> player = playerService.getPlayerById(id);
            if (player.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(player.get(), HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed fetching all players", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeamById(@PathVariable(required = false) UUID playerId) {
        try {
            boolean removed = playerService.deletePlayer(playerId);
            if (removed) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed player team with id {}", playerId, t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPlayerById(@Valid @NotNull @PathVariable(required = true) Player player) {
        try {
            boolean added = playerService.addPlayer(player);
            if (!added) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed adding player", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePlayerById(@Valid @NotNull @PathVariable(required = true) Player player) {
        try {
            if (Objects.isNull(player.getId())) {
                log.error("Player ID was not provided");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            boolean added = playerService.updatePlayer(player);
            if (!added) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed updating player", t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
