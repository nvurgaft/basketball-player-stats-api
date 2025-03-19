package com.nvurgaft.basketball_api.repositories;

import com.nvurgaft.basketball_api.mappers.PlayerRowMapper;
import com.nvurgaft.basketball_api.mappers.PlayerStatsRowMapper;
import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.model.PlayerGameStats;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Repository
public class JdbcStatsRepository implements GenericRepository<PlayerGameStats, UUID> {

    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(PlayerGameStats stats) {
        return jdbcTemplate.update("INSERT INTO stats (id, player, game, stats) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                UUID.randomUUID(), stats.getId(), stats.getPlayer(),
                stats.getPoints(), stats.getRebounds(), stats.getAssists(), stats.getSteals(), stats.getBlocks(),
                stats.getFouls(), stats.getTurnovers(), stats.getMinutesPlayed());
    }

    @Override
    public int saveAll(List<PlayerGameStats> entities) {
        return 0;
    }

    @Override
    public int update(PlayerGameStats stats) {
        return jdbcTemplate.update("UPDATE stats SET player=?, points=?, rebounds=?, assists=?, steals=?, blocks=?, fouls=?, turnovers=?, minutes_played=? WHERE id=?",
                stats.getId(), stats.getPlayer(),
                stats.getPoints(), stats.getRebounds(), stats.getAssists(), stats.getSteals(), stats.getBlocks(),
                stats.getFouls(), stats.getTurnovers(), stats.getMinutesPlayed(), stats.getId().toString());
    }

    @Override
    public Optional<PlayerGameStats> findById(UUID id) {
        PlayerGameStats stats = jdbcTemplate.queryForObject("SELECT * FROM stats WHERE id=?",
                new PlayerStatsRowMapper(), id);
        return Optional.ofNullable(stats);
    }

    @Override
    public int deleteById(UUID id) {
        return jdbcTemplate.update("DELETE FROM stats WHERE id=?", id.toString());
    }

    @Override
    public List<PlayerGameStats> findAll() {
        return jdbcTemplate.query("SELECT * from stats",
                new PlayerStatsRowMapper());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("DELETE from stats");
    }
}
