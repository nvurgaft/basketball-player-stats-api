package com.nvurgaft.basketball_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Season {

    private UUID id;
    private int year;
}
