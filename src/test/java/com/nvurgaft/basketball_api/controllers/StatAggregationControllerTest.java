package com.nvurgaft.basketball_api.controllers;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.model.PlayerStats;
import com.nvurgaft.basketball_api.model.StatsAggregation;
import com.nvurgaft.basketball_api.model.Team;
import com.nvurgaft.basketball_api.services.AggregationsService;
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

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatAggregationControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TeamService teamService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private AggregationsService aggregationsService;

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
    void shouldGetPlayerSeasonAverage() {
        playerService.addPlayer(new Player("Michael", "Jordan"));
        teamService.addTeam(new Team("Chicago Bulls"));

        Player michaelJordan = playerService.getPlayerByName("Michael", "Jordan")
                .orElseThrow(() -> new RuntimeException("Failed fetching player"));

        Team chicagoBulls = teamService.getTeamByName("Chicago Bulls")
                .orElseThrow(() -> new RuntimeException("Failed fetching team"));

        int season = 1999;
        List<PlayerStats> stats = List.of(
                new PlayerStats(michaelJordan,
                        chicagoBulls,
                        season,
                        22, 4, 2, 6, 12, 4, 1, 24),
                new PlayerStats(michaelJordan,
                        chicagoBulls,
                        season,
                        17, 4, 2, 6, 16, 2, 2, 47)
        );
        statsService.addStats(stats);

        aggregationsService.getPlayerSeasonAverage(michaelJordan.getId(), season);

        StatsAggregation responseBody = given()
                .contentType(ContentType.JSON)
                .queryParam("playerId", michaelJordan.getId().toString())
                .queryParam("season", season)
                .when()
                .get("/api/v1/aggs/player/average")
                .as(StatsAggregation.class);

        Assertions.assertEquals(3, responseBody.getFouls(), 0.1);
        Assertions.assertEquals(4, responseBody.getRebounds(), 0.1);
        Assertions.assertEquals(14, responseBody.getBlocks(), 0.1);
    }

    @Test
    void shouldGetTeamSeasonAverage() {

        teamService.addTeam(new Team("Chicago Bulls"));
        playerService.addPlayers(List.of(
                new Player("Michael", "Jordan"),
                new Player("Dennis", "Rodman"),
                new Player("Scottie", "Pippen")));

        Player michaelJordan = playerService.getPlayerByName("Michael", "Jordan")
                .orElseThrow(() -> new RuntimeException("Failed fetching player"));
        Player dennisRodman = playerService.getPlayerByName("Dennis", "Rodman")
                .orElseThrow(() -> new RuntimeException("Failed fetching player"));
        Player scottiePippen = playerService.getPlayerByName("Scottie", "Pippen")
                .orElseThrow(() -> new RuntimeException("Failed fetching player"));

        Team chicagoBulls = teamService.getTeamByName("Chicago Bulls")
                .orElseThrow(() -> new RuntimeException("Failed fetching team"));

        int season = 1999;
        List<PlayerStats> stats = List.of(
                new PlayerStats(michaelJordan,
                        chicagoBulls,
                        season,
                        34, 4, 2, 6, 9, 2, 1, 24),
                new PlayerStats(michaelJordan,
                        chicagoBulls,
                        season,
                        31, 3, 2, 6, 13, 1, 2, 47),
                new PlayerStats(dennisRodman,
                        chicagoBulls,
                        season,
                        22, 7, 2, 6, 12, 4, 1, 24),
                new PlayerStats(dennisRodman,
                        chicagoBulls,
                        season,
                        17, 11, 2, 6, 16, 5, 2, 47),
                new PlayerStats(scottiePippen,
                        chicagoBulls,
                        season,
                        24, 5, 2, 6, 8, 1, 1, 24)
        );
        statsService.addStats(stats);

        aggregationsService.getTeamSeasonAverage(chicagoBulls.getId(), season);

        StatsAggregation responseBody = given()
                .contentType(ContentType.JSON)
                .queryParam("teamId", chicagoBulls.getId().toString())
                .queryParam("season", season)
                .when()
                .get("/api/v1/aggs/team/average")
                .as(StatsAggregation.class);

        Assertions.assertEquals(2.6, responseBody.getFouls(), 0.1);
        Assertions.assertEquals(6, responseBody.getRebounds(), 0.1);
        Assertions.assertEquals(11.6, responseBody.getBlocks(), 0.1);
    }
}
