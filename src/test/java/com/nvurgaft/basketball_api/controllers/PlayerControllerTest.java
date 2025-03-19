package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.services.PlayerService;
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
public class PlayerControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private PlayerService playerService;

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
    }

    @Test
    void shouldGetAllPlayers() {
        List<Player> players = List.of(
                new Player(UUID.randomUUID(), "Michael", "Jordan", "Chicago Bulls"),
                new Player(UUID.randomUUID(), "Scottie", "Pippen", "Chicago Bulls"),
                new Player(UUID.randomUUID(), "Dennis", "Rodman", "Chicago Bulls")
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
        Player player1 = new Player(UUID.randomUUID(), "Michael", "Jordan", "Chicago Bulls");
        Player player2 = new Player(UUID.randomUUID(), "Scottie", "Pippen", "Chicago Bulls");
        Player player3 = new Player(UUID.randomUUID(), "Dennis", "Rodman", "Chicago Bulls");
        playerService.addPlayers(List.of(player1, player2, player3));

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", player2.getId().toString())
                .when()
                .get("/api/v1/players/{id}")
                .then()
                .statusCode(200)
                .body("name", org.hamcrest.Matchers.containsString("Scottie"));
    }
}
