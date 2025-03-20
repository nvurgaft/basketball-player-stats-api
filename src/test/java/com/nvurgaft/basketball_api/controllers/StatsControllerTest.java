package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.model.PlayerStats;
import com.nvurgaft.basketball_api.model.StatsAggregation;
import com.nvurgaft.basketball_api.model.Team;
import com.nvurgaft.basketball_api.services.PlayerService;
import com.nvurgaft.basketball_api.services.StatsService;
import com.nvurgaft.basketball_api.services.TeamService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
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

    @Test
    void shouldFailValidation() {
        Player player = new Player(UUID.randomUUID(), "Michael", "Jordan");
        Team team = new Team(UUID.randomUUID(), "Washington Wizards");

        PlayerStats playerStats = new PlayerStats(UUID.randomUUID(),
                player, team,
                1998, 22, 4, 2, 6, 12, 999, 1, 24);

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

    @Test
    void shouldGetPlayerSeasonAverage() {
        Player player = new Player(UUID.randomUUID(), "Michael", "Jordan");
        Team team = new Team(UUID.randomUUID(), "Chicago Bulls");
        int season = 1999;

        List<PlayerStats> stats = List.of(
                new PlayerStats(UUID.randomUUID(), player,
                        team,
                        season,
                        22, 4, 2, 6, 12, 4, 1, 24),
                new PlayerStats(UUID.randomUUID(), player,
                        team,
                        season,
                        17, 4, 2, 6, 16, 2, 2, 47)
        );
        playerService.addPlayer(player);
        teamService.addTeam(team);
        statsService.addStats(stats);

        statsService.getPlayerSeasonAverage(player.getId(), season);

        StatsAggregation responseBody = given()
                .contentType(ContentType.JSON)
                .queryParam("playerId", player.getId().toString())
                .queryParam("season", season)
                .when()
                .get("/api/v1/stats/player/average")
                .as(StatsAggregation.class);

        Assertions.assertEquals(3, responseBody.getFouls(), 0.1);
        Assertions.assertEquals(4, responseBody.getRebounds(), 0.1);
        Assertions.assertEquals(14, responseBody.getBlocks(), 0.1);
    }

    @Test
    void shouldGetTeamSeasonAverage() {
        Player michaelJordan = new Player(UUID.randomUUID(), "Michael", "Jordan");
        Player dennisRodman = new Player(UUID.randomUUID(), "Dennis", "Rodman");
        Player scottiePippen = new Player(UUID.randomUUID(), "Scottie", "Pippen");
        Team chicagoBulls = new Team(UUID.randomUUID(), "Chicago Bulls");
        int season = 1999;

        List<PlayerStats> stats = List.of(
                new PlayerStats(UUID.randomUUID(), michaelJordan,
                        chicagoBulls,
                        season,
                        34, 4, 2, 6, 9, 2, 1, 24),
                new PlayerStats(UUID.randomUUID(), michaelJordan,
                        chicagoBulls,
                        season,
                        31, 3, 2, 6, 13, 1, 2, 47),
                new PlayerStats(UUID.randomUUID(), dennisRodman,
                        chicagoBulls,
                        season,
                        22, 7, 2, 6, 12, 4, 1, 24),
                new PlayerStats(UUID.randomUUID(), dennisRodman,
                        chicagoBulls,
                        season,
                        17, 11, 2, 6, 16, 5, 2, 47),
                new PlayerStats(UUID.randomUUID(), scottiePippen,
                        chicagoBulls,
                        season,
                        24, 5, 2, 6, 8, 1, 1, 24)
        );
        playerService.addPlayers(List.of(michaelJordan, dennisRodman, scottiePippen));
        teamService.addTeam(chicagoBulls);
        statsService.addStats(stats);

        statsService.getTeamSeasonAverage(chicagoBulls.getId(), season);

        StatsAggregation responseBody = given()
                .contentType(ContentType.JSON)
                .queryParam("teamId", chicagoBulls.getId().toString())
                .queryParam("season", season)
                .when()
                .get("/api/v1/stats/team/average")
                .as(StatsAggregation.class);

        Assertions.assertEquals(2.6, responseBody.getFouls(), 0.1);
        Assertions.assertEquals(6, responseBody.getRebounds(), 0.1);
        Assertions.assertEquals(11.6, responseBody.getBlocks(), 0.1);
    }

}
