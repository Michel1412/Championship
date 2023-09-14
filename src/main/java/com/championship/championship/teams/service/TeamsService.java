package com.championship.championship.teams.service;

import com.championship.championship.teams.Teams;
import com.championship.championship.teams.repository.TeamsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TeamsService {

    private final TeamsRepository teamsRepository;

    public TeamsService(TeamsRepository teamsRepository) {
        this.teamsRepository = teamsRepository;
    }

    public Object createTeams(Teams teams) {
        this.validateTeamName(teams.getName());
        Teams team = new Teams();
        team.setName(teams.getName());
        return ResponseEntity.ok(this.teamsRepository.save(team));
    }

    private void validateTeamName(String name) {
        if (this.teamsRepository.findByName(name)) {
            throw new RuntimeException("Esse nome de time já foi cadastrado!");    
        }
    }
}
