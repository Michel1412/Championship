package com.championship.championship.championships.service;

import com.championship.championship.championships.Championship;
import com.championship.championship.championships.repository.ChampionshipRepository;
import com.championship.championship.classifications_table.ClassificationsTable;
import com.championship.championship.classifications_table.classificationsTableDTO.ClassificationTableDTO;
import com.championship.championship.classifications_table.service.ClassificationsTableService;
import com.championship.championship.matches.repository.MatchesRepository;
import com.championship.championship.teams.Teams;
import com.championship.championship.teams.repository.TeamsRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Service
public class ChampionshipService {

    private final MatchesRepository matchesRepository;
    private final ChampionshipRepository championshipRepository;
    private final ClassificationsTableService classificationsTableService;
    private final TeamsRepository teamsRepository;
    public ChampionshipService(MatchesRepository matchesRepository, ChampionshipRepository championshipRepository, ClassificationsTableService classificationsTableService, TeamsRepository teamsRepository) {
        this.matchesRepository = matchesRepository;
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
        Integer[] teams = classificationTableDTO.getListTeams();
        this.createTableWithTeams(teams, championship);
        championship.setChampionshipStarted(true);
        this.championshipRepository.save(championship);
        return ResponseEntity.ok("Campeonato: " + championship.getChampionshipName() + " acabou de começar se prepare!");
    }

    public Object updateChampionship(Integer id, Championship championship) {
        Championship updateChampionship = this.validateExistsChampionship(id);
        this.validateChampionship(championship);
        try {
            this.validIfItAlreadyStarted(updateChampionship);
        } catch (RuntimeException re){
            throw new RuntimeException(re.getMessage() + " É impossível alterar!");
        }
        updateChampionship.setChampionshipName(championship.getChampionshipName());
        updateChampionship.setChampionshipYear(championship.getChampionshipYear());
        return ResponseEntity.ok(this.championshipRepository.save(updateChampionship));
    }

    public Object finishChampionship(Integer id) {
        Championship championship = this.validateExistsChampionship(id);
        this.validPlaysOffs(championship);
        championship.setChampionshipFinished(true);
        this.championshipRepository.save(championship);
        return ResponseEntity.ok("O campeonato " + championship.getChampionshipName() + " terminou! Foi bom enquanto durou, tempo de descanço!");
    }

    public Object deleteChampionship(Integer id) {
        Championship championship = this.validateExistsChampionship(id);
        try {
            this.validIfItAlreadyStarted(championship);
        } catch (RuntimeException re) {
            throw new RuntimeException(re.getMessage() + " Impossivel deletar!");
        }
        this.championshipRepository.delete(championship);
        return ResponseEntity.ok("Campeonato deletado com sucesso!");
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

    private void validPlaysOffs(Championship championship) {
        if (!championship.isChampionshipStarted()) {
            throw new RuntimeException("Esse campeonato não pode terminar, ele não foi começado ainda!");
        }
        if (championship.isChampionshipFinished()) {
            throw new RuntimeException("Esse campeonato já temrinou!");
        }
        this.validateHowMuchPlaysOffs(championship);
    }

    private void validateHowMuchPlaysOffs(Championship championship) {
        int allPlaysOff = this.classificationsTableService.countTeamsOfChampionship(championship.getChampionshipId());
        int playsPlayed = this.matchesRepository.countPlaysPlayedOfChampionship(championship);
        if (!(allPlaysOff == playsPlayed)) {
            throw new RuntimeException("Não é possivel o campeonato ainda é necessário fazer mais jogos");
        }
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
        if (Objects.isNull(championship.getChampionshipYear())) {
            championship.setChampionshipYear(date.get(1));
        }
        if (championship.getChampionshipYear() < date.get(1)) {
            throw new RuntimeException("Não é possível criar um campeonato nesse ano!");
        }
    }

    public List<Championship> findAllByYear(int year) {
        return this.championshipRepository.findAllByYear(year);
    }

    public List<ClassificationsTable> findAllTeamsByChampionship(Integer id) {
        this.validateExistsChampionship(id);
        return this.classificationsTableService.listAllTeamsByChampionship(id);
    }
}
