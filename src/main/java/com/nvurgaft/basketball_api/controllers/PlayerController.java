package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.model.PlayerStats;
import com.nvurgaft.basketball_api.services.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@Log4j2
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

    @GetMapping("/season/{id}")
    public ResponseEntity<PlayerStats> getPlayerSeasonStats() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/team/{id}")
    public ResponseEntity<List<Player>> getPlayersByTeam() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
