package com.nvurgaft.basketball_api.services;

import com.nvurgaft.basketball_api.repositories.JdbcStatsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatsService {

    private JdbcStatsRepository repository;
}
