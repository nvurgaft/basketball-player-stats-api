package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.StatsAggregation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("api/v1/stats")
public class StatsController {

    @GetMapping("/player/average/{id}")
    public ResponseEntity<StatsAggregation> getPlayerSeasonAverage(@PathVariable(required = true) UUID playerId) {

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/team/average/{id}")
    public ResponseEntity<StatsAggregation> getTeamSeasonAverage(@PathVariable(required = true) UUID teamId) {

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
