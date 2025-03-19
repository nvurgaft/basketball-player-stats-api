package com.nvurgaft.basketball_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
@Data
@AllArgsConstructor
public class Team {

    private UUID id;
    private String name;
}
