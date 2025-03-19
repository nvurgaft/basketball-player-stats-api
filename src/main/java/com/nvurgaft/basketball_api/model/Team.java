package com.nvurgaft.basketball_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Team {

    private UUID id;
    private String name;
}
