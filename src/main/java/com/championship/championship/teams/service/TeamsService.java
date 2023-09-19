package com.championship.championship.teams.service;

import com.championship.championship.classifications_table.repository.ClassificationsTableRepository;
import com.championship.championship.classifications_table.service.ClassificationsTableService;
import com.championship.championship.matches.repository.MatchesRepository;
import com.championship.championship.teams.Teams;
import com.championship.championship.teams.repository.TeamsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TeamsService {

    private final TeamsRepository teamsRepository;
    private final MatchesRepository matchesRepository;
    private final ClassificationsTableRepository classificationsTableRepository;


    public TeamsService(TeamsRepository teamsRepository, MatchesRepository matchesRepository, ClassificationsTableRepository classificationsTableRepository) {
        this.teamsRepository = teamsRepository;
        this.matchesRepository = matchesRepository;
        this.classificationsTableRepository = classificationsTableRepository;
    }

    public Object createTeams(Teams teams) {
        this.validateTeamName(teams.getName());
        return ResponseEntity.ok(this.teamsRepository.save(teams));
    }

    public Object updateTeam(Integer id, Teams teams) {
        Teams team = this.findTeamById(id);
        this.validateTeamName(teams.getName());
        team.setName(teams.getName());
        return ResponseEntity.ok(this.teamsRepository.save(team));
    }

    public Object deleteTeam(Integer id) {
        this.findTeamById(id);
        if (this.classificationsTableRepository.countTeamsOnTablesById(id)) {
            throw new RuntimeException("O time já faz parte de uma tabela!");
        }
        if (this.matchesRepository.findMatchById(id)) {
            throw new RuntimeException("O time já jogou ou tem um jogo para fazer!");
        };
        this.teamsRepository.deleteById(id);
        return ResponseEntity.ok("Time deletado com sucesso!");
    }

    private Teams findTeamById(Integer id) {
        return this.teamsRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("Time não encontrado!");
        });
    }

    private void validateTeamName(String name) {
        if (this.teamsRepository.findByName(name)) {
            throw new RuntimeException("Esse nome de time já foi cadastrado!");
        }
    }




}
