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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
}
