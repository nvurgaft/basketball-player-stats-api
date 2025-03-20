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
import java.util.UUID;

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
        statsService.deleteAll();
    }

    @Test
    void shouldGetAllStats() {
        Player player = new Player(UUID.randomUUID(), "Michael", "Jordan");
        Team team1 = new Team(UUID.randomUUID(), "Washington Wizards");
        Team team2 = new Team(UUID.randomUUID(), "Chicago Bulls");

        List<PlayerStats> stats = List.of(
                new PlayerStats(UUID.randomUUID(), player,
                        team1,
                        1998,
                        22, 4, 2, 6, 12, 4, 1, 24),
                new PlayerStats(UUID.randomUUID(), player,
                        team2,
                        1999,
                        17, 4, 2, 6, 16, 2, 2, 47)
        );
        playerService.addPlayer(player);
        teamService.addTeams(List.of(team1, team2));

        statsService.addStats(stats);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/stats/")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

}
