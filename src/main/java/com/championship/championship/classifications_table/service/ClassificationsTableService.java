package com.championship.championship.classifications_table.service;

import com.championship.championship.championships.Championship;
import com.championship.championship.championships.repository.ChampionshipRepository;
import com.championship.championship.classifications_table.ClassificationsTable;
import com.championship.championship.classifications_table.repository.ClassificationsTableRepository;
import com.championship.championship.teams.Teams;
import com.championship.championship.teams.repository.TeamsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

@Service
public class ClassificationsTableService {

    private final TeamsRepository teamsRepository;
    private final ChampionshipRepository championshipRepository;
    private final ClassificationsTableRepository classificationsTableRepository;

    public ClassificationsTableService(TeamsRepository teamsRepository, ChampionshipRepository championshipRepository, ClassificationsTableRepository classificationsTableRepository) {
        this.teamsRepository = teamsRepository;
        this.championshipRepository = championshipRepository;
        this.classificationsTableRepository = classificationsTableRepository;
    }

    public Object joinOnChampionship(Integer teamId, Integer championshipId) {
        ClassificationsTable tableForSave = this.validateJoinOnChampionship(teamId, championshipId);
        return ResponseEntity.ok(this.classificationsTableRepository.save(tableForSave));
    }

    private ClassificationsTable createObjectToSave(Integer teamId, Integer championshipId) {
        Teams team = this.teamsRepository.findById(teamId).orElseThrow(() -> {
            return new RuntimeException("Time não encontrado!");});
        Championship championship = this.championshipRepository.findById(championshipId).orElseThrow(() -> {
            return new RuntimeException("Campeonato não encontrado!");});
        return this.createObject(team, championship);
    }

    private ClassificationsTable createObject(Teams team, Championship championship) {
        this.validateIfChampionshipStart(championship);
        this.validadeIfTeamAlreadyJoin(team, championship);
        ClassificationsTable tableForSave = new ClassificationsTable();
        tableForSave.setChampionship(championship);
        tableForSave.setTeams(team);
        tableForSave.setDraw(0);
        tableForSave.setWins(0);
        tableForSave.setLoses(0);
        tableForSave.setPoints(0);
        tableForSave.setGoalsScored(0);
        tableForSave.setGoalsConceded(0);
        tableForSave.setGoalsdifference(0);
        return tableForSave;
    }

    private void validadeIfTeamAlreadyJoin(Teams team, Championship championship) {
        if (this.classificationsTableRepository.findByTeamId(team, championship)) {
            throw new RuntimeException("Esse time já esta nesse campeonato!");
        }
    }

    private ClassificationsTable validateJoinOnChampionship(Integer teamId, Integer championshipId) {
        ClassificationsTable tableForSave = this.createObjectToSave(teamId, championshipId);
        try {

        } catch (NullPointerException e){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return tableForSave;
    }

    private void validateIfChampionshipStart(Championship championship) {
        if (championship.isChampionshipStarted()) {
            throw new RuntimeException("O campeonato já começou, não é possivel entrar no meio do campeonato!");
        }
    }

}
