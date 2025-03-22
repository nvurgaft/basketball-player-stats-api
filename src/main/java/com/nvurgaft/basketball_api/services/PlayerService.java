package com.nvurgaft.basketball_api.services;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.repositories.JdbcPlayerRepository;
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
public class PlayerService {

    private JdbcPlayerRepository repository;

    @Cacheable(value = "players", key = "'all'")
    public List<Player> getAll() {
        return repository.findAll();
    }

    @Cacheable(value = "players", key = "#id")
    public Optional<Player> getPlayerById(@NonNull UUID id) {
        return repository.findById(id);
    }

    @Cacheable(value = "players", key = "#name + '-' + #surname")
    public Optional<Player> getPlayerByName(@NonNull String name, String surname) {
        return repository.findByName(name, surname);
    }

    public void addPlayers(@NonNull List<Player> players) {
        repository.saveAll(players);
    }

    public boolean addPlayer(@NonNull Player player) {
        int affected = repository.save(player);
        return affected == 1;
    }

    @CachePut(cacheNames = "players", key = "#player.id")
    public boolean updatePlayer(@NonNull Player player) {
        int affected = repository.save(player);
        return affected == 1;
    }

    @CacheEvict(value = "players", key = "#id")
    public boolean deletePlayer(UUID id) {
        int affected = repository.deleteById(id);
        return affected == 1;
    }

    @CacheEvict(value = "players", allEntries = true)
    public void deleteAll() {
        repository.deleteAll();
    }

}
