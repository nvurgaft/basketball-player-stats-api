package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.model.PlayerStats;
import com.nvurgaft.basketball_api.model.Team;
import com.nvurgaft.basketball_api.services.PlayerService;
import com.nvurgaft.basketball_api.services.StatsService;
import com.nvurgaft.basketball_api.services.TeamService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatsControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TeamService teamService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private StatsService statsService;

    static PostgreSQLContainer<?> pgContainer = new PostgreSQLContainer<>(
            "postgres:17-alpine"
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", pgContainer::getJdbcUrl);
        registry.add("spring.datasource.username", pgContainer::getUsername);
        registry.add("spring.datasource.password", pgContainer::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        pgContainer.start();
    }

    @AfterAll
    static void afterAll() {
        pgContainer.stop();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:%1$s".formatted(port);
        playerService.deleteAll();
        teamService.deleteAll();
        statsService.deleteAll();
    }

    @Test
    void shouldGetAllStats() {
        Player player = new Player("Michael", "Jordan");
        playerService.addPlayer(player);
        Team team1 = new Team("Washington Wizards");
        Team team2 = new Team("Chicago Bulls");
        teamService.addTeams(List.of(team1, team2));

        Optional<Player> playerOptional = playerService.getPlayerByName("Michael", "Jordan");
        Optional<Team> teamOptional1 = teamService.getTeamByName("Washington Wizards");
        Optional<Team> teamOptional2 = teamService.getTeamByName("Chicago Bulls");

        List<PlayerStats> stats = List.of(
                new PlayerStats(playerOptional.orElseThrow(() -> new RuntimeException("Failed fetching player")),
                        teamOptional1.orElseThrow(() -> new RuntimeException("Failed fetching team")),
                        1998,
                        22, 4, 2, 6, 12, 4, 1, 24),
                new PlayerStats(playerOptional.orElseThrow(() -> new RuntimeException("Failed fetching player")),
                        teamOptional2.orElseThrow(() -> new RuntimeException("Failed fetching team")),
                        1999,
                        17, 4, 2, 6, 16, 2, 2, 47)
        );
        statsService.addStats(stats);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/stats/")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

    @Test
    void shouldFailValidation() {
        Player player = new Player();
        player.setName("Michael");
        player.setSurname("Jordan");
        Team team = new Team();
        team.setName("Washington Wizards");

        PlayerStats playerStats = new PlayerStats();
        playerStats.setPlayer(player);
        playerStats.setTeam(team);
        playerStats.setSeason(1998);
        playerStats.setAssists(12);
        playerStats.setFouls(9999);
        playerStats.setBlocks(12);
        playerStats.setMinutesPlayed(43);
        playerStats.setRebounds(11);

        playerService.addPlayer(player);
        teamService.addTeam(team);

        given()
                .contentType(ContentType.JSON)
                .body(playerStats)
                .when()
                .post("/api/v1/stats/")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON);
    }

}
