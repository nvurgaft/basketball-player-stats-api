package com.nvurgaft.basketball_api.services;

import com.nvurgaft.basketball_api.model.Player;
import com.nvurgaft.basketball_api.model.Team;
import com.nvurgaft.basketball_api.repositories.JdbcPlayerRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PlayerService {

    private JdbcPlayerRepository repository;

    public List<Player> getAll() {
        // TODO: dev only, remove afterwards
        return repository.findAll();
    }

    public Player getPlayersByTeam(@NonNull Team team) {
        // TODO:
        return null;
    }

    public Optional<Player> getPlayerById(@NonNull UUID id) {
        return repository.findById(id);
    }

    public void addPlayers(@NonNull List<Player> players) {
        repository.saveAll(players);
    }

    public void addPlayer(@NonNull Player player) {
        repository.save(player);
    }

    public void updatePlayer(@NonNull Player player) {
        repository.save(player);
    }

    public void deletePlayer(UUID playerId) {
        repository.deleteById(playerId);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

}
