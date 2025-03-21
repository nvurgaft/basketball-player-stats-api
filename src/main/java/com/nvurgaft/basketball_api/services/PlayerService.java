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

    @Cacheable(value = "players", key="'all'")
    public List<Player> getAll() {
        // TODO: dev only, remove afterwards
        return repository.findAll();
    }

    @Cacheable(value = "players", key="#id")
    public Optional<Player> getPlayerById(@NonNull UUID id) {
        return repository.findById(id);
    }

    @Cacheable(value = "players", key="#name + '-' + #surname")
    public Optional<Player> getPlayerByName(@NonNull String name, String surname) {
        return repository.findByName(name, surname);
    }

    public void addPlayers(@NonNull List<Player> players) {
        repository.saveAll(players);
    }

    public void addPlayer(@NonNull Player player) {
        repository.save(player);
    }

    @CachePut(cacheNames="players", key="#player.id")
    public void updatePlayer(@NonNull Player player) {
        repository.save(player);
    }

    @CacheEvict(value = "players", key = "#id")
    public void deletePlayer(UUID id) {
        repository.deleteById(id);
    }

    @CacheEvict(value = "players", allEntries = true)
    public void deleteAll() {
        repository.deleteAll();
    }

}
