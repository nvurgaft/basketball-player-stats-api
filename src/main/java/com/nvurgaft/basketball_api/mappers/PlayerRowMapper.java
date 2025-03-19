package com.nvurgaft.basketball_api.mappers;

import com.nvurgaft.basketball_api.model.Player;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerRowMapper implements RowMapper<Player>  {
    @Override
    public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID id = rs.getObject("id", UUID.class);
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String team = rs.getString("team");
        return new Player(id, name, surname, team);
    }
}
