package com.nvurgaft.basketball_api.repositories;

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
public class JdbcTeamRepository implements GenericRepository<Team, UUID> {

    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Team team) {
        return jdbcTemplate.update("INSERT INTO teams (name) VALUES (?)",
                team.getName());
    }

    @Override
    public int saveAll(List<Team> teams) {
        jdbcTemplate.batchUpdate("INSERT INTO teams (name) VALUES (?)",
                teams,
                100,
                (PreparedStatement ps, Team team) -> {
                    ps.setString(1, team.getName());
                });
        return teams.size();
    }

    @Override
    public int update(Team team) {
        return jdbcTemplate.update("UPDATE teams SET name=? WHERE id=?",
                team.getName());
    }

    @Override
    public Optional<Team> findById(UUID id) {
        Team team = jdbcTemplate.queryForObject("SELECT * FROM teams WHERE id=?",
                new TeamRowMapper(), id);
        return Optional.ofNullable(team);
    }

    public Optional<Team> findByName(String name) {
        Team team = jdbcTemplate.queryForObject("SELECT * FROM teams WHERE name=?",
                new TeamRowMapper(), name);
        return Optional.ofNullable(team);
    }

    @Override
    public List<Team> findAll() {
        return jdbcTemplate.query("SELECT * from teams",
                new TeamRowMapper());
    }

    @Override
    public int deleteById(UUID id) {
        return jdbcTemplate.update("DELETE FROM teams WHERE id=?", id.toString());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("TRUNCATE TABLE teams RESTART IDENTITY CASCADE");
    }

    private static class TeamRowMapper implements RowMapper<Team> {
        @Override
        public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
            UUID id = rs.getObject("id", UUID.class);
            String name = rs.getString("name");
            return new Team(id, name);
        }
    }
}
