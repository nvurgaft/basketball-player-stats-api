package com.nvurgaft.basketball_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    private UUID id;
    private String name;
    private String surname;
    private Team team;
}
