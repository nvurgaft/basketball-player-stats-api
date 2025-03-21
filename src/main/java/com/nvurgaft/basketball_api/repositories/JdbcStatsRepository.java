package com.nvurgaft.basketball_api.repositories;

import com.nvurgaft.basketball_api.model.*;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Repository
public class JdbcStatsRepository implements GenericRepository<PlayerStats, UUID> {

    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(PlayerStats stats) {
        return jdbcTemplate.update("INSERT INTO stats (player_id, team_id, season, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                stats.getPlayer().getId(), stats.getTeam().getId(), stats.getSeason(),
                stats.getPoints(), stats.getRebounds(), stats.getAssists(), stats.getSteals(), stats.getBlocks(),
                stats.getFouls(), stats.getTurnovers(), stats.getMinutesPlayed());
    }

    @Override
    public int saveAll(List<PlayerStats> stats) {
        jdbcTemplate.batchUpdate("INSERT INTO stats (player_id, team_id, season, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                stats,
                100,
                (PreparedStatement ps, PlayerStats playerStats) -> {
                    ps.setObject(1, playerStats.getPlayer().getId());
                    ps.setObject(2, playerStats.getTeam().getId());
                    ps.setInt(3, playerStats.getSeason());
                    ps.setInt(4, playerStats.getPoints());
                    ps.setInt(5, playerStats.getRebounds());
                    ps.setInt(6, playerStats.getAssists());
                    ps.setInt(7, playerStats.getSteals());
                    ps.setInt(8, playerStats.getBlocks());
                    ps.setInt(9, playerStats.getFouls());
                    ps.setInt(10, playerStats.getTurnovers());
                    ps.setFloat(11, playerStats.getMinutesPlayed());
                });
        return stats.size();
    }

    @Override
    public int update(PlayerStats stats) {
        return jdbcTemplate.update("UPDATE stats SET player_id=?, team_id=?, season=?, points=?, rebounds=?, assists=?, steals=?, blocks=?, fouls=?, turnovers=?, minutes_played=? WHERE id=?",
                stats.getPlayer().getId(), stats.getTeam().getId(), stats.getSeason(),
                stats.getPoints(), stats.getRebounds(), stats.getAssists(), stats.getSteals(), stats.getBlocks(),
                stats.getFouls(), stats.getTurnovers(), stats.getMinutesPlayed(), stats.getId().toString());
    }

    @Override
    public Optional<PlayerStats> findById(UUID id) {
        PlayerStats stats = jdbcTemplate.queryForObject("SELECT t.id, t.name, " +
                        "p.id, p.name, p.surname, " +
                        "s.id, s.player_id, s.team_id, s.season, s.points, s.rebounds, s.assists, s.steals, s.blocks, s.fouls, s.turnovers, s.minutes_played " +
                        "FROM stats s " +
                        "JOIN players p ON s.player_id = p.id " +
                        "JOIN teams t ON s.team_id = t.id " +
                        "WHERE s.id = ?;",
                new PlayerStatsJoinRowMapper(), id);
        return Optional.ofNullable(stats);
    }

    public List<PlayerStats> findByPlayerName(String name, String surname) {
        return jdbcTemplate.query("SELECT t.id, t.name, " +
                        "p.id, p.name, p.surname, " +
                        "s.id, s.player_id, s.team_id, s.season, s.points, s.rebounds, s.assists, s.steals, s.blocks, s.fouls, s.turnovers, s.minutes_played " +
                        "FROM stats s " +
                        "JOIN players p ON s.player_id = p.id " +
                        "JOIN teams t ON s.team_id = t.id " +
                        "WHERE p.name = ? AND p.surname = ?",
                new PlayerStatsJoinRowMapper(), name, surname);
    }

    @Override
    public List<PlayerStats> findAll() {
        return jdbcTemplate.query("SELECT t.id, t.name, " +
                        "p.id, p.name, p.surname, " +
                        "s.id, s.player_id, s.team_id, s.season, s.points, s.rebounds, s.assists, s.steals, s.blocks, s.fouls, s.turnovers, s.minutes_played " +
                        "FROM stats s " +
                        "JOIN players p ON s.player_id = p.id " +
                        "JOIN teams t ON s.team_id = t.id;",
                new PlayerStatsJoinRowMapper());
    }

    public Optional<StatsAggregation> getPlayerSeasonAverage(UUID playerId, int season) {
        StatsAggregation aggregation = jdbcTemplate.queryForObject("SELECT " +
                        "AVG(s.points), AVG(s.rebounds), AVG(s.assists), AVG(s.steals), AVG(s.blocks), AVG(s.fouls), AVG(s.turnovers), AVG(s.minutes_played) " +
                        "FROM stats s " +
                        "JOIN players p ON s.player_id = p.id " +
                        "JOIN teams t ON s.team_id = t.id " +
                        "WHERE p.id = ? AND s.season = ?;",
                new StatsAggregationRowMapper(), playerId, season);
        return Optional.ofNullable(aggregation);
    }


    public Optional<StatsAggregation> getTeamSeasonAverage(UUID teamId, int season) {
        StatsAggregation aggregation = jdbcTemplate.queryForObject("SELECT " +
                        "AVG(s.points), AVG(s.rebounds), AVG(s.assists), AVG(s.steals), AVG(s.blocks), AVG(s.fouls), AVG(s.turnovers), AVG(s.minutes_played) " +
                        "FROM stats s " +
                        "JOIN players p ON s.player_id = p.id " +
                        "JOIN teams t ON s.team_id = t.id " +
                        "WHERE t.id = ? AND s.season = ?;",
                new StatsAggregationRowMapper(), teamId, season);
        return Optional.ofNullable(aggregation);
    }

    @Override
    public int deleteById(UUID id) {
        return jdbcTemplate.update("DELETE FROM stats WHERE id=?", id.toString());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("TRUNCATE TABLE stats RESTART IDENTITY CASCADE");
    }

    private static class StatsAggregationRowMapper implements RowMapper<StatsAggregation> {

        @Override
        public StatsAggregation mapRow(ResultSet rs, int rowNum) throws SQLException {
            float points = rs.getFloat(1);
            float rebounds = rs.getFloat(2);
            float assists = rs.getFloat(3);
            float steals = rs.getFloat(4);
            float blocks = rs.getFloat(5);
            float fouls = rs.getFloat(6);
            float turnovers = rs.getFloat(7);
            float minutesPlayed = rs.getFloat(8);
            return new StatsAggregation(points, rebounds, assists, steals, blocks, fouls, turnovers, minutesPlayed);
        }
    }

    private static class PlayerStatsJoinRowMapper implements RowMapper<PlayerStats> {

        @Override
        public PlayerStats mapRow(ResultSet rs, int rowNum) throws SQLException {
            UUID teamId = rs.getObject(1, UUID.class);
            String teamName = rs.getString(2);
            Team team = new Team(teamId, teamName);

            UUID playerId = rs.getObject(3, UUID.class);
            String playerName = rs.getString(4);
            String playerSurname = rs.getString(5);
            Player player = new Player(playerId, playerName, playerSurname);

            UUID statsId = rs.getObject(6, UUID.class);
            int season = rs.getInt(9);
            int points = rs.getInt(10);
            int rebounds = rs.getInt(11);
            int assists = rs.getInt(12);
            int steals = rs.getInt(13);
            int blocks = rs.getInt(14);
            int fouls = rs.getInt(15);
            int turnovers = rs.getInt(16);
            float minutesPlayed = rs.getFloat(17);
            return new PlayerStats(statsId, player, team, season, points, rebounds, assists, steals, blocks, fouls, turnovers, minutesPlayed);
        }
    }
}
