package com.nvurgaft.basketball_api.services;

import com.nvurgaft.basketball_api.model.PlayerStats;
import com.nvurgaft.basketball_api.model.StatsAggregation;
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

    @Cacheable(value = "stats", key = "#name + '-' + #surname")
    public List<PlayerStats> getPlayerStats(String name, String surname) {
        return repository.findByPlayerName(name, surname);
    }

    @Cacheable(value = "stats", key = "#stat.id")
    public Optional<PlayerStats> getStatsById(@NonNull UUID id) {
        return repository.findById(id);
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
        int result = repository.deleteById(id);
        return result == 1;
    }

    @CacheEvict(value = "stats", allEntries = true)
    public void deleteAll() {
        repository.deleteAll();
    }
}
