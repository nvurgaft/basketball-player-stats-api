package com.nvurgaft.basketball_api.services;

import com.nvurgaft.basketball_api.model.PlayerStats;
import com.nvurgaft.basketball_api.repositories.JdbcStatsRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StatsService {

    private JdbcStatsRepository repository;

    @Cacheable(value = "stats", key = "#stat.id")
    public Optional<PlayerStats> getStatsById(@NonNull UUID id) {
        return repository.findById(id);
    }

    @Cacheable(value = "stats", key = "#player.id")
    public List<PlayerStats> getStatsByPlayerId(@NonNull UUID id) {
        return repository.findByPlayerId(id);
    }

    @Cacheable(value = "stats", key = "#playerId + '-' + #teamId")
    public List<PlayerStats> getPlayerTeamStats(@NonNull UUID playerId, @NonNull UUID teamId) {
        return repository.findPlayerTeamStatsById(playerId, teamId);
    }

    @Cacheable(value = "stats", key = "#playerId + '-' + #season")
    public List<PlayerStats> getPlayerSeasonStats(@NonNull UUID id, int season) {
        return repository.findPlayerSeasonStatsById(id, season);
    }

    public List<PlayerStats> getAllStats() {
        return repository.findAll();
    }

    public boolean addStat(@NonNull PlayerStats stat) {
        int result = repository.save(stat);
        return result == 1;
    }

    public boolean addStats(@NonNull List<PlayerStats> stats) {
        int result = repository.saveAll(stats);
        return result == 1;
    }

    @CachePut(cacheNames = "stats", key = "#stat.id")
    public boolean updateStat(@NonNull PlayerStats stat) {
        int result = repository.update(stat);
        return result == 1;
    }

    @CacheEvict(value = "stats", key = "#stat.id")
    public boolean deleteStat(@NonNull UUID id) {
        int affected = repository.deleteById(id);
        return affected == 1;
    }

    @CacheEvict(value = "stats", allEntries = true)
    public void deleteAll() {
        repository.deleteAll();
    }
}
