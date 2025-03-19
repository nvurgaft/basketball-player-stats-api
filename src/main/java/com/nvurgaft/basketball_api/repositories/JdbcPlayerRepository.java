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
        return jdbcTemplate.update("INSERT INTO players (id, name, surname) VALUES (?, ?, ?)",
                player.getId(), player.getName(), player.getSurname());
    }

    @Override
    public int saveAll(List<Player> players) {
        jdbcTemplate.batchUpdate("INSERT INTO players (id, name, surname, team_id) VALUES (?, ?, ?, ?)",
                players,
                100,
                (PreparedStatement ps, Player player) -> {
                    ps.setObject(1, player.getId());
                    ps.setString(2, player.getName());
                    ps.setString(3, player.getSurname());
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
        Player player = jdbcTemplate.queryForObject("SELECT * FROM players WHERE p.id=?",
                new RowMapper<Player>() {
                    @Override
                    public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
                        UUID id = rs.getObject("id", UUID.class);
                        String name = rs.getString("name");
                        String surname = rs.getString("surname");
                        return new Player(id, name, surname);
                    }
                }, id);
        return Optional.ofNullable(player);
    }

    @Override
    public int deleteById(UUID id) {
        return jdbcTemplate.update("DELETE FROM players WHERE id=?", id);
    }

    @Override
    public List<Player> findAll() {
        return jdbcTemplate.query("SELECT * FROM players",
                new RowMapper<Player>() {
                    @Override
                    public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
                        UUID id = rs.getObject("id", UUID.class);
                        String name = rs.getString("name");
                        String surname = rs.getString("surname");
                        return new Player(id, name, surname);
                    }
                });
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("DELETE from players");
    }

}
