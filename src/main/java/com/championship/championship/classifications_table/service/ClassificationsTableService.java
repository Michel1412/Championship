package com.championship.championship.classifications_table.service;

import com.championship.championship.championships.Championship;
import com.championship.championship.classifications_table.ClassificationsTable;
import com.championship.championship.classifications_table.repository.ClassificationsTableRepository;
import com.championship.championship.teams.Teams;
import org.springframework.stereotype.Service;

@Service
public class ClassificationsTableService {

    private final ClassificationsTableRepository classificationsTableRepository;

    public ClassificationsTableService(ClassificationsTableRepository classificationsTableRepository) {
        this.classificationsTableRepository = classificationsTableRepository;
    }

    public void createObjectToSave(Teams team, Championship championship) {
        ClassificationsTable teamForSave = new ClassificationsTable();
        teamForSave.setChampionship(championship);
        teamForSave.setTeams(team);
        teamForSave.setDraw(0);
        teamForSave.setWins(0);
        teamForSave.setLoses(0);
        teamForSave.setPoints(0);
        teamForSave.setGoalsScored(0);
        teamForSave.setGoalsConceded(0);
        teamForSave.setGoalsdifference(0);
        this.classificationsTableRepository.save(teamForSave);
    }

    public boolean validadeIfTeamAlreadyJoin(Integer teamId, Integer championshipId) {
        return this.classificationsTableRepository.findByTeamIdAndChampionship(teamId, championshipId);
    }

    public Integer countTeamsOfChampionship(Integer championshipId) {
        int totalTeams = this.classificationsTableRepository.countTeamsOfChampionship(championshipId);
        return totalTeams * (totalTeams - 1 );
    }

}
