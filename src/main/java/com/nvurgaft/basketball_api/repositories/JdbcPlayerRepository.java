package com.nvurgaft.basketball_api.repositories;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.model.Team;
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
        return jdbcTemplate.update("INSERT INTO players (id, name, surname, team_id) VALUES (?, ?, ?, ?)",
                player.getId(), player.getName(), player.getSurname(), player.getTeam().getId());
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
                    ps.setObject(4, player.getTeam().getId());
                });
        return players.size();
    }

    @Override
    public int update(Player player) {
        return jdbcTemplate.update("UPDATE players SET name=?, surname=?, team_id=? WHERE id=?",
                player.getName(), player.getSurname(), player.getTeam().getId());
    }

    @Override
    public Optional<Player> findById(UUID id) {
        Player player = jdbcTemplate.queryForObject("SELECT p.id, p.name, p.surname, p.team_id, " +
                        "t.id, t.name " +
                        "FROM players p " +
                        "JOIN teams t ON p.team_id = t.id " +
                        "WHERE p.id=?",
                new RowMapper<Player>() {
                    @Override
                    public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
                        UUID id = rs.getObject(1, UUID.class);
                        String name = rs.getString(2);
                        String surname = rs.getString(3);
                        UUID teamId = rs.getObject(4, UUID.class);
                        String teamName = rs.getString(5);
                        return new Player(id, name, surname, new Team(teamId, teamName));
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
        return jdbcTemplate.query("SELECT p.id, p.name, p.surname, p.team_id, " +
                        "t.id, t.name " +
                        "FROM players p " +
                        "JOIN teams t ON p.team_id = t.id",
                new RowMapper<Player>() {
                    @Override
                    public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
                        UUID id = rs.getObject(1, UUID.class);
                        String name = rs.getString(2);
                        String surname = rs.getString(3);
                        UUID teamId = rs.getObject(4, UUID.class);
                        String teamName = rs.getString(5);
                        return new Player(id, name, surname, new Team(teamId, teamName));
                    }
                });
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("DELETE from players");
    }
}
