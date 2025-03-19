package com.nvurgaft.basketball_api.mappers;

import com.nvurgaft.basketball_api.model.PlayerGameStats;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerStatsRowMapper implements RowMapper<PlayerGameStats> {
    @Override
    public PlayerGameStats mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID id = rs.getObject("id", UUID.class);
        String player = rs.getString("player");
        String game = rs.getString("game");
        int points = rs.getInt("points");
        int rebounds = rs.getInt("rebounds");
        int assists = rs.getInt("assists");
        int steals = rs.getInt("steals");
        int blocks = rs.getInt("blocks");
        int fouls = rs.getInt("fouls");
        int turnovers = rs.getInt("turnovers");
        float minutesPlayed = rs.getFloat("minutes_played");
        return new PlayerGameStats(id, null, null, points, rebounds, assists, steals, blocks, fouls, turnovers, minutesPlayed);
    }
}
