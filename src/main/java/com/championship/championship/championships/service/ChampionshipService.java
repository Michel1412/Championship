package com.championship.championship.championships.service;

import com.championship.championship.championships.Championship;
import com.championship.championship.championships.repository.ChampionshipRepository;
import com.championship.championship.classifications_table.classificationsTableDTO.ClassificationTableDTO;
import com.championship.championship.classifications_table.service.ClassificationsTableService;
import com.championship.championship.teams.Teams;
import com.championship.championship.teams.repository.TeamsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Calendar;

@Service
public class ChampionshipService {

    private final ChampionshipRepository championshipRepository;
    private final ClassificationsTableService classificationsTableService;
    private final TeamsRepository teamsRepository;
    public ChampionshipService(ChampionshipRepository championshipRepository, ClassificationsTableService classificationsTableService, TeamsRepository teamsRepository) {
        this.championshipRepository = championshipRepository;
        this.classificationsTableService = classificationsTableService;
        this.teamsRepository = teamsRepository;
    }

    public Object createChampionship(Championship championship) {
        this.validateChampionship(championship);
        return ResponseEntity.ok(this.championshipRepository.save(championship));
    }

    public Object startChampionship(Integer id, ClassificationTableDTO classificationTableDTO) {
        Championship championship = this.validateExistsChampionship(id);
        this.validIfItAlreadyStarted(championship);
//        this.validTheChampionshipIsInThisYear();
        Integer[] teams = classificationTableDTO.getListTeams();
        this.createTableWithTeams(teams, championship);
        championship.setChampionshipStarted(true);
        this.championshipRepository.save(championship);
        return ResponseEntity.ok("Campeonato: " + championship.getChampionshipName() + " acabou de começar se prepare!");
    }

    private void createTableWithTeams(Integer[] teams, Championship championship) {
        for (Integer teamsId:teams) {
            Teams team = this.teamsRepository.findById(teamsId).orElseThrow(() -> {
                return new RuntimeException("Time com id: " + teamsId + " não foi encontrado!");
            });
            if (!this.classificationsTableService.validadeIfTeamAlreadyJoin(teamsId,championship.getChampionshipId())){
                this.classificationsTableService.createObjectToSave(team, championship);
            }
        }
    }

    private void validIfItAlreadyStarted(Championship championship) {
        if (championship.isChampionshipStarted()) {
            throw new RuntimeException("Esse campeonato já começou!");
        }
    }

    public Object finishChampionship(Integer id) {
        Championship championship = this.validateExistsChampionship(id);
        championship.setChampionshipFinished(true);
        this.championshipRepository.save(championship);
        return ResponseEntity.ok("O campeonato " + championship.getChampionshipName() + " terminou! Foi bom enquanto durou tempo de descanço!");
    }

    private Championship validateExistsChampionship(Integer id) {
        return  this.championshipRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("Campeonato não encontrado!");
        });
    }

    private void validateChampionship(Championship championship) {
        this.validateDateChampionship(championship);
        this.validateDateAndNameChampionship(championship);
    }

    private void validateDateAndNameChampionship(Championship championship) {
        if (this.championshipRepository.countChampionshipByNameAndYear(championship.getChampionshipName(),championship.getChampionshipYear())) {
            throw new RuntimeException("Esse campeonato já foi criado no ano de " + championship.getChampionshipYear());
        }
    }

    private void validateDateChampionship(Championship championship) {
        Calendar date = Calendar.getInstance();
        if (championship.getChampionshipYear() < date.get(1)) {
            throw new RuntimeException("Não é possível criar um campeonato nesse ano!");
        }
    }
}
