package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.Team;
import com.nvurgaft.basketball_api.services.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@Log4j2
@AllArgsConstructor
@RequestMapping("api/v1/teams")
public class TeamController {

    private TeamService teamService;

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

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable(required = false) UUID id) {
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

    @PostMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addTeam(
            @Valid @NotNull(message = "Team cannot be null") @RequestBody Team team
    ) {
        try {
            boolean added = teamService.addTeam(team);
            if (!added) {
                log.error("Failed storing team data");
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (Throwable t) {
            log.error("Failed adding team with id {}", team.getId(), t);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
