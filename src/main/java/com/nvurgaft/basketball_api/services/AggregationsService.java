package com.nvurgaft.basketball_api.services;

import com.nvurgaft.basketball_api.model.StatsAggregation;
import com.nvurgaft.basketball_api.repositories.JdbcStatsRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AggregationsService {

    private JdbcStatsRepository repository;

    @Cacheable(value = "averages", key = "#playerId + '-' + #seasonYear")
    public Optional<StatsAggregation> getPlayerSeasonAverage(UUID playerId, int seasonYear) {
        return repository.getPlayerSeasonAverage(playerId, seasonYear);
    }

    @Cacheable(value = "averages", key = "#teamId + '-' + #seasonYear")
    public Optional<StatsAggregation> getTeamSeasonAverage(UUID teamId, int seasonYear) {
        return repository.getTeamSeasonAverage(teamId, seasonYear);
    }

}
