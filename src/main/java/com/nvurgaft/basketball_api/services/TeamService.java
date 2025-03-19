package com.nvurgaft.basketball_api.services;

import com.nvurgaft.basketball_api.model.Team;
import com.nvurgaft.basketball_api.repositories.GenericRepository;
import com.nvurgaft.basketball_api.repositories.JdbcTeamRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TeamService {

    private JdbcTeamRepository repository;

    public Optional<Team> getTeamById(@NonNull UUID id) {
        return repository.findById(id);
    }

    public List<Team> getAllTeams() {
        return repository.findAll();
    }

    public boolean addTeam(@NonNull Team team) {
        int result = repository.save(team);
        return result == 1;
    }

    public boolean addTeams(@NonNull List<Team> team) {
        int result = repository.saveAll(team);
        return result == 1;
    }

    public boolean updateTeam(@NonNull Team team) {
        int result = repository.update(team);
        return result == 1;
    }

    public boolean deleteTeam(@NonNull UUID teamId) {
        int result = repository.deleteById(teamId);
        return result == 1;
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
