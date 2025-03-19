package com.nvurgaft.basketball_api.repositories;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.model.PlayerStats;
import com.nvurgaft.basketball_api.model.Season;
import com.nvurgaft.basketball_api.model.Team;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
        return jdbcTemplate.update("INSERT INTO stats (id, player, game, stats) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                stats.getId(), stats.getId(), stats.getPlayer(),
                stats.getPoints(), stats.getRebounds(), stats.getAssists(), stats.getSteals(), stats.getBlocks(),
                stats.getFouls(), stats.getTurnovers(), stats.getMinutesPlayed());
    }

    @Override
    public int saveAll(List<PlayerStats> entities) {
        return 0;
    }

    @Override
    public int update(PlayerStats stats) {
        return jdbcTemplate.update("UPDATE stats SET player=?, points=?, rebounds=?, assists=?, steals=?, blocks=?, fouls=?, turnovers=?, turnovers=? WHERE id=?",
                stats.getId(), stats.getPlayer(),
                stats.getPoints(), stats.getRebounds(), stats.getAssists(), stats.getSteals(), stats.getBlocks(),
                stats.getFouls(), stats.getTurnovers(), stats.getMinutesPlayed(), stats.getId().toString());
    }

    @Override
    public Optional<PlayerStats> findById(UUID id) {
        PlayerStats stats = jdbcTemplate.queryForObject("SELECT t.id, t.name, " +
                        "p.id, p.name, p.surname, p.team_id, " +
                        "sn.id, sn.name, " +
                        "st.points, st.rebounds, st.assists, st.steals, st.blocks, st.fouls, st.turnovers, st.turnovers " +
                        "FROM stats st " +
                        "JOIN players p ON st.player_id = p.id " +
                        "JOIN teams t ON st.team_id = t.id " +
                        "JOIN seasons sn ON ts.season_id = sn.id " +
                        "WHERE id=?",
                new PlayerStatsJoinRowMapper(), id);
        return Optional.ofNullable(stats);
    }

    @Override
    public List<PlayerStats> findAll() {
        return jdbcTemplate.query("SELECT t.id, t.name, " +
                            "p.id, p.name, p.surname, p.team_id, " +
                            "sn.id, sn.name, " +
                            "st.points, st.rebounds, st.assists, st.steals, st.blocks, st.fouls, st.turnovers, st.turnovers " +
                        "FROM stats st " +
                        "JOIN players p ON st.player_id = p.id " +
                        "JOIN teams t ON st.team_id = t.id " +
                        "JOIN seasons sn ON ts.season_id = sn.id",
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
            String playerName = rs.getString(3);
            String playerSurname = rs.getString(4);
            Player player = new Player(playerId, playerName, playerSurname);

            UUID seasonId = rs.getObject(5, UUID.class);
            int seasonYear = rs.getInt(6);
            Season season = new Season(seasonId, seasonYear);

            UUID statsId = rs.getObject(7, UUID.class);
            int points = rs.getInt(8);
            int rebounds = rs.getInt(9);
            int assists = rs.getInt(10);
            int steals = rs.getInt(11);
            int blocks = rs.getInt(12);
            int fouls = rs.getInt(13);
            int turnovers = rs.getInt(14);
            float minutesPlayed = rs.getFloat(15);
            return new PlayerStats(statsId, player, team, season, points, rebounds, assists, steals, blocks, fouls, turnovers, minutesPlayed);
        }
    }
}
