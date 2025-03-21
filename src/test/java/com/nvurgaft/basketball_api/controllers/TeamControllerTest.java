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
import java.util.Optional;

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
    void shouldGetAllTeams() {
        List<Team> teams = List.of(
                new Team("Chicago Bulls"),
                new Team("Miami Heat"),
                new Team("Charlotte Hornets"),
                new Team("New York Knicks")
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
    void shouldGetTeamById() {
        Team team1 = new Team("Miami Heat");
        Team team2 = new Team("Los Angeles Lakers");
        teamService.addTeams(List.of(team1, team2));

        Optional<Team> teamOptional = teamService.getTeamByName("Los Angeles Lakers");
        Team team = teamOptional.orElseThrow(() -> new NullPointerException("No team found"));

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", team.getId().toString())
                .when()
                .get("/api/v1/teams/{id}")
                .then()
                .statusCode(200)
                .body("name", org.hamcrest.Matchers.containsString("Los Angeles Lakers"));
    }

    @Test
    void shouldPostAValidTeam() {
        Team team = new Team("Miami Heat");

        given()
                .contentType(ContentType.JSON)
                .body(team)
                .when()
                .post("/api/v1/teams/")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldFailAddingInvalidTeam() {
        // no team name, should fail
        Team team = new Team(null);

        given()
                .contentType(ContentType.JSON)
                .body(team)
                .when()
                .post("/api/v1/teams/")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON);
    }

}
