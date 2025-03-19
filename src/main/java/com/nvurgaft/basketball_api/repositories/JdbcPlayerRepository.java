package com.nvurgaft.basketball_api.repositories;

import com.nvurgaft.basketball_api.mappers.PlayerRowMapper;
import com.nvurgaft.basketball_api.model.Player;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Repository
public class JdbcPlayerRepository implements GenericRepository<Player, UUID> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Player player) {
        return jdbcTemplate.update("INSERT INTO players (id, name, surname, team) VALUES (?, ?, ?, ?)",
                UUID.randomUUID(), player.getName(), player.getSurname(), player.getTeam());
    }

    @Override
    public int saveAll(List<Player> players) {
        jdbcTemplate.batchUpdate("INSERT INTO players (id, name, surname, team) VALUES (?, ?, ?, ?)",
                players,
                100,
                (PreparedStatement ps, Player player) -> {
                    ps.setObject(1, player.getId());
                    ps.setString(2, player.getName());
                    ps.setString(3, player.getSurname());
                    ps.setString(4, player.getTeam());
                });
        return players.size();
    }

    @Override
    public int update(Player player) {
        return jdbcTemplate.update("UPDATE players SET name=?, surname=?, team=? WHERE id=?",
                player.getName(), player.getSurname(), player.getTeam());
    }

    @Override
    public Optional<Player> findById(UUID id) {
        Player player = jdbcTemplate.queryForObject("SELECT * FROM players WHERE id=?",
                new PlayerRowMapper(), id);
        return Optional.ofNullable(player);
    }

    @Override
    public int deleteById(UUID id) {
        return jdbcTemplate.update("DELETE FROM players WHERE id=?", id.toString());
    }

    @Override
    public List<Player> findAll() {
        return jdbcTemplate.query("SELECT * from players",
                new PlayerRowMapper());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("DELETE from players");
    }
}
