package com.nvurgaft.basketball_api.repositories;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.model.PlayerStats;
import com.nvurgaft.basketball_api.model.Team;
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
        return jdbcTemplate.update("INSERT INTO stats (id, player_id, team_id, season, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                stats.getId(), stats.getPlayer().getId(), stats.getSeason(),
                stats.getPoints(), stats.getRebounds(), stats.getAssists(), stats.getSteals(), stats.getBlocks(),
                stats.getFouls(), stats.getTurnovers(), stats.getMinutesPlayed());
    }

    @Override
    public int saveAll(List<PlayerStats> stats) {
        jdbcTemplate.batchUpdate("INSERT INTO stats (id, player_id, team_id, season, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                stats,
                100,
                (PreparedStatement ps, PlayerStats playerStats) -> {
                    ps.setObject(1, playerStats.getId());
                    ps.setObject(2, playerStats.getPlayer().getId());
                    ps.setObject(3, playerStats.getTeam().getId());
                    ps.setInt(4, playerStats.getSeason());
                    ps.setInt(5, playerStats.getPoints());
                    ps.setInt(6, playerStats.getRebounds());
                    ps.setInt(7, playerStats.getAssists());
                    ps.setInt(8, playerStats.getSteals());
                    ps.setInt(9, playerStats.getBlocks());
                    ps.setInt(10, playerStats.getFouls());
                    ps.setInt(11, playerStats.getTurnovers());
                    ps.setFloat(12, playerStats.getMinutesPlayed());
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

    @Override
    public int deleteById(UUID id) {
        return jdbcTemplate.update("DELETE FROM stats WHERE id=?", id.toString());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("DELETE from stats");
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
