package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.Team;
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
public class TeamControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TeamService teamService;

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
        teamService.deleteAll();
    }

    @Test
    void shouldGetAllPlayers() {
        List<Team> teams = List.of(
                new Team(UUID.randomUUID(), "Chicago Bulls"),
                new Team(UUID.randomUUID(), "Miami Heat"),
                new Team(UUID.randomUUID(), "Charlotte Hornets"),
                new Team(UUID.randomUUID(), "New York Knicks")
        );
        teamService.addTeams(teams);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/teams/")
                .then()
                .statusCode(200)
                .body(".", hasSize(4));
    }

    @Test
    void shouldGetPlayerById() {
        Team team1 = new Team(UUID.randomUUID(), "Miami Heat");
        Team team2 = new Team(UUID.randomUUID(), "Los Angeles Lakers");
        teamService.addTeams(List.of(team1, team2));

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", team2.getId().toString())
                .when()
                .get("/api/v1/teams/{id}")
                .then()
                .statusCode(200)
                .body("name", org.hamcrest.Matchers.containsString("Los Angeles Lakers"));
    }

}
