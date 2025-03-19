package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.model.PlayerStats;
import com.nvurgaft.basketball_api.model.Season;
import com.nvurgaft.basketball_api.model.Team;
import com.nvurgaft.basketball_api.services.StatsService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

public class StatsControllerTest {

    @LocalServerPort
    private Integer port;

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
        List<PlayerStats> stats = List.of(
                new PlayerStats(UUID.randomUUID(), player,
                        new Team(UUID.randomUUID(), "Washington Wizards"),
                        new Season(UUID.randomUUID(), 1998),
                        7, 4, 2, 6, 12, 4, 1, 24),
                new PlayerStats(UUID.randomUUID(), player,
                        new Team(UUID.randomUUID(), "Chicago Bulls"),
                        new Season(UUID.randomUUID(), 1999),
                        7, 4, 2, 6, 12, 4, 1, 24)
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
    void shouldGetStatById() {
        Player player = new Player(UUID.randomUUID(), "Michael", "Jordan");
        UUID secondStatId = UUID.randomUUID();
        List<PlayerStats> stats = List.of(
                new PlayerStats(UUID.randomUUID(), player,
                        new Team(UUID.randomUUID(), "Washington Wizards"),
                        new Season(UUID.randomUUID(), 1998),
                        7, 4, 2, 6, 12, 4, 1, 24),
                new PlayerStats(secondStatId, player,
                        new Team(UUID.randomUUID(), "Chicago Bulls"),
                        new Season(UUID.randomUUID(), 1999),
                        7, 4, 2, 6, 12, 4, 1, 24),
                new PlayerStats(UUID.randomUUID(), player,
                        new Team(UUID.randomUUID(), "Chicago Bulls"),
                        new Season(UUID.randomUUID(), 2000),
                        7, 4, 2, 6, 12, 4, 1, 24)
        );
        statsService.addStats(stats);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", secondStatId.toString())
                .when()
                .get("/api/v1/stats/{id}")
                .then()
                .statusCode(200)
                .body("name", org.hamcrest.Matchers.containsString("Los Angeles Lakers"));
    }

}
