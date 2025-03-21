package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.model.Team;
import com.nvurgaft.basketball_api.services.PlayerService;
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
public class PlayerControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private PlayerService playerService;

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
        playerService.deleteAll();
        teamService.deleteAll();
    }

    @Test
    void shouldGetAllPlayers() {
        Team team = new Team("Chicago Bulls");
        teamService.addTeam(team);

        List<Player> players = List.of(
                new Player("Michael", "Jordan"),
                new Player("Scottie", "Pippen"),
                new Player("Dennis", "Rodman")
        );
        playerService.addPlayers(players);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/players/")
                .then()
                .statusCode(200)
                .body(".", hasSize(3));
    }

    @Test
    void shouldGetPlayerById() {
        Team team = new Team("Chicago Bulls 1");
        teamService.addTeam(team);

        Player player1 = new Player("Michael", "Jordan");
        Player player2 = new Player("Scottie", "Pippen");
        Player player3 = new Player("Dennis", "Rodman");
        playerService.addPlayers(List.of(player1, player2, player3));

        Optional<Player> playerOptional = playerService.getPlayerByName("Scottie", "Pippen");
        Player targetPlayer = playerOptional.orElseThrow(() -> new RuntimeException("Player not found"));
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", targetPlayer.getId().toString())
                .when()
                .get("/api/v1/players/{id}")
                .then()
                .statusCode(200)
                .body("name", org.hamcrest.Matchers.containsString("Scottie"));
    }
}
