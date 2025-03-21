package com.nvurgaft.basketball_api.repositories;

import com.nvurgaft.basketball_api.model.Player;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class JdbcPlayerRepository implements GenericRepository<Player, UUID> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Player player) {
        return jdbcTemplate.update("INSERT INTO players (name, surname) VALUES (?, ?)",
                player.getName(), player.getSurname());
    }

    @Override
    public int saveAll(List<Player> players) {
        jdbcTemplate.batchUpdate("INSERT INTO players (name, surname) VALUES (?, ?)",
                players,
                100,
                (PreparedStatement ps, Player player) -> {
                    ps.setString(1, player.getName());
                    ps.setString(2, player.getSurname());
                });
        return players.size();
    }

    @Override
    public int update(Player player) {
        return jdbcTemplate.update("UPDATE players SET name=?, surname=? WHERE id=?",
                player.getName(), player.getSurname());
    }

    @Override
    public Optional<Player> findById(UUID id) {
        Player player = jdbcTemplate.queryForObject("SELECT * FROM players WHERE id=?",
                new PlayerRowMapper(), id);
        return Optional.ofNullable(player);
    }

    public Optional<Player> findByName(String name, String surname) {
        Player player = jdbcTemplate.queryForObject("SELECT * FROM players WHERE name=? AND surname=?",
                new PlayerRowMapper(), name, surname);
        return Optional.ofNullable(player);
    }

    @Override
    public int deleteById(UUID id) {
        return jdbcTemplate.update("DELETE FROM players WHERE id=?", id);
    }

    @Override
    public List<Player> findAll() {
        return jdbcTemplate.query("SELECT * FROM players",
                new PlayerRowMapper());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("TRUNCATE TABLE players RESTART IDENTITY CASCADE");
    }

    private static class PlayerRowMapper implements RowMapper<Player> {
        @Override
        public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
            UUID id = rs.getObject("id", UUID.class);
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            return new Player(id, name, surname);
        }
    }
}
