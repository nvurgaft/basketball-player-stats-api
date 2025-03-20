package com.nvurgaft.basketball_api.services;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.model.Team;
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

@AllArgsConstructor
@Service
public class PlayerService {

    private JdbcPlayerRepository repository;

    @Cacheable(value = "players", key="'all'")
    public List<Player> getAll() {
        // TODO: dev only, remove afterwards
        return repository.findAll();
    }


    @Cacheable(value = "players", key="#team.id")
    public Player getPlayersByTeam(@NonNull Team team) {
        // TODO:
        return null;
    }

    @Cacheable(value = "players", key="#player.d")
    public Optional<Player> getPlayerById(@NonNull UUID id) {
        return repository.findById(id);
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

    @CacheEvict(value = "players", key = "#player.id")
    public void deletePlayer(UUID playerId) {
        repository.deleteById(playerId);
    }

    @CacheEvict(value = "players", allEntries = true)
    public void deleteAll() {
        repository.deleteAll();
    }

}
