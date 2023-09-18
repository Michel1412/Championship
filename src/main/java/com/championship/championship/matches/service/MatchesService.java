package com.championship.championship.matches.service;

import com.championship.championship.championships.Championship;
import com.championship.championship.championships.repository.ChampionshipRepository;
import com.championship.championship.classifications_table.ClassificationsTable;
import com.championship.championship.classifications_table.repository.ClassificationsTableRepository;
import com.championship.championship.matches.Matches;
import com.championship.championship.matches.matchesDTO.MatchesDTO;
import com.championship.championship.matches.repository.MatchesRepository;
import com.championship.championship.teams.Teams;
import com.championship.championship.teams.repository.TeamsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

@Service
public class MatchesService {
    private final MatchesRepository matchesRepository;
    private final TeamsRepository teamsRepository;
    private final ChampionshipRepository championshipRepository;
    private final ClassificationsTableRepository classificationsTableRepository;

    public MatchesService(MatchesRepository matchesRepository, TeamsRepository teamsRepository, ChampionshipRepository championshipRepository, ClassificationsTableRepository classificationsTableRepository) {
        this.matchesRepository = matchesRepository;
        this.teamsRepository = teamsRepository;
        this.championshipRepository = championshipRepository;
        this.classificationsTableRepository = classificationsTableRepository;
    }

    public Object createMatch(MatchesDTO matchesDTO) {
        Matches matches = this.matchToSave(matchesDTO);
        return ResponseEntity.ok(this.matchesRepository.save(matches));
    }

    public Object startMatch(Integer id) {
        Matches matches = this.findMatch(id);
        if (matches.isMatchStarted()) {
            throw new RuntimeException("Essa partida já começou!");
        }
        if (matches.isMatchFinished()) {
            throw new RuntimeException("Essa partida já terminou!");
        }
        Random gerador = new Random();
        matches.setVistingTeamGoals(gerador.nextInt(8));
        matches.setHomeTeamGoals(gerador.nextInt(8));
        matches.setMatchStarted(true);
        this.matchesRepository.save(matches);
        return ResponseEntity.ok("Partida começada com sucesso!");
    }

    public Object finishMatch(Integer id) {
        Matches matches = this.findMatch(id);
        if (!matches.isMatchStarted()) {
            throw new RuntimeException("Essa partida não começou!");
        }
        if (matches.isMatchFinished()) {
            throw new RuntimeException("Essa partida já terminou!");
        }
        if (Objects.nonNull(matches.getChampionshipId())) {
            this.putPointsForTeams(matches.getChampionshipId(), matches.getHomeTeam(), matches.getVisitingTeam(), matches);
        }
        matches.setMatchFinished(true);
        this.matchesRepository.save(matches);
        return ResponseEntity.ok("Piiiiiiiii! Apita o juiz, fim de jogo!");
    }

    private void putPointsForTeams(Championship championshipId, Teams homeTeam, Teams visitingTeam, Matches match) {
        Integer tableHomeTeamId = this.classificationsTableRepository.pickByTeamIdAndChampionship(homeTeam.getId(), championshipId.getChampionshipId());
        Integer tableVisitingTeamId = this.classificationsTableRepository.pickByTeamIdAndChampionship(visitingTeam.getId(), championshipId.getChampionshipId());
        ClassificationsTable tableHomeTeamForSave = this.classificationsTableRepository.findById(tableHomeTeamId).orElseThrow(() -> {
            return new RuntimeException("Classificação do time da casa não encontrada!");
        });
        ClassificationsTable tableVisitingTeamForSave = this.classificationsTableRepository.findById(tableVisitingTeamId).orElseThrow(() -> {
            return new RuntimeException("Classificação do time visitante não encontrada!");
        });
        this.putGoalsInTables(match, tableHomeTeamForSave, tableVisitingTeamForSave);
    }

    private void putGoalsInTables(Matches match, ClassificationsTable tableHomeTeamForSave, ClassificationsTable tableVisitingTeamForSave) {
        if (match.getHomeTeamGoals() < match.getVistingTeamGoals()) {
            tableVisitingTeamForSave.setPoints(tableVisitingTeamForSave.getPoints() + 3);
            tableVisitingTeamForSave.setWins(tableVisitingTeamForSave.getWins() + 1);
            tableHomeTeamForSave.setLoses(tableHomeTeamForSave.getLoses() + 1);

        } else if (match.getVistingTeamGoals() < match.getHomeTeamGoals()) {
            tableHomeTeamForSave.setPoints(tableHomeTeamForSave.getPoints() + 3 );
            tableVisitingTeamForSave.setLoses(tableVisitingTeamForSave.getLoses() + 1);
            tableHomeTeamForSave.setWins(tableHomeTeamForSave.getWins() + 1);

        } else {
            tableVisitingTeamForSave.setPoints(tableVisitingTeamForSave.getPoints() + 1);
            tableHomeTeamForSave.setPoints(tableHomeTeamForSave.getPoints() + 1);
            tableVisitingTeamForSave.setDraw(tableVisitingTeamForSave.getDraw() + 1);
            tableHomeTeamForSave.setDraw(tableHomeTeamForSave.getDraw() + 1);
        }
        tableHomeTeamForSave.setGoalsScored(        tableHomeTeamForSave.getGoalsScored()       + match.getHomeTeamGoals());
        tableHomeTeamForSave.setGoalsConceded(      tableHomeTeamForSave.getGoalsConceded()     + match.getVistingTeamGoals());
        tableHomeTeamForSave.setGoalsdifference(    tableHomeTeamForSave.getGoalsScored()       - tableHomeTeamForSave.getGoalsConceded());

        tableVisitingTeamForSave.setGoalsScored(    tableVisitingTeamForSave.getGoalsScored()   + match.getVistingTeamGoals());
        tableVisitingTeamForSave.setGoalsConceded(  tableVisitingTeamForSave.getGoalsConceded() + match.getHomeTeamGoals());
        tableVisitingTeamForSave.setGoalsdifference(tableVisitingTeamForSave.getGoalsScored()   - tableVisitingTeamForSave.getGoalsConceded());
    }

    private Matches findMatch(Integer id) {
        return this.matchesRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("Partida não encontrada!");
        });
    }

    private Matches matchToSave(MatchesDTO matchesDTO) {
        Matches matches = new Matches();
        Teams homeTeam = this.validTeams(matchesDTO.getHomeTeamId());
        Teams visitingTeam = this.validTeams(matchesDTO.getVisitingTeamId());
        this.validateTeamsCanPlay(homeTeam, visitingTeam, matchesDTO.getMatchDate());
        if (Objects.nonNull(matchesDTO.getChampionshipId())) {
            matches.setChampionshipId(this.validChampionshipId(matchesDTO.getChampionshipId(), homeTeam, visitingTeam));
        }
        matches.setHomeTeam(homeTeam);
        matches.setVisitingTeam(visitingTeam);
        matches.setMatchDate(matchesDTO.getMatchDate());
        return matches;
    }

    private void validateTeamsCanPlay(Teams homeTeam, Teams visitingTeam, Calendar matchDate) {
        Calendar minimusDate = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        yesterday.set(2,matchDate.get(2));
        yesterday.set(3, matchDate.get(3));
        yesterday.set(7, matchDate.get(7) - 1);
        tomorrow.set(2,matchDate.get(2));
        tomorrow.set(3, matchDate.get(3));
        tomorrow.set(7, matchDate.get(7) + 1);
        String msg = "";
        if (homeTeam.equals(visitingTeam)) {
            throw new RuntimeException("Não é possivel marcar jogos entre times iguais!");
        }
        if ((minimusDate.get(6) + 3) > matchDate.get(6) && minimusDate.get(2) == matchDate.get(2)) {
            msg = "Muito proximo do dia de hoje!";
        }
        if (this.matchesRepository.findMatchDate(matchDate,tomorrow,yesterday, homeTeam)) {
            msg = "Muito proximo da data de outo jogo do time da casa!";
        }
        if (this.matchesRepository.findMatchDate(matchDate,tomorrow,yesterday, visitingTeam)) {
            msg = "Muito proximo da data de outo jogo do time visitante!";
        }
        if (!msg.isEmpty()) {
            throw new RuntimeException("Não foi possivel marcar o jogo. " + msg);
        }
    }

    private Championship validChampionshipId(Integer championshipId, Teams homeTeam, Teams visitingTeam) {
        Championship championship = this.championshipRepository.findById(championshipId).orElseThrow(() -> {
            return new RuntimeException("Campeonato não encontrado!");
        });

        if (!championship.isChampionshipStarted() || championship.isChampionshipFinished()) {
            String msg = "Não é possível marcar jogos nesse campeonato!";
            if (championship.isChampionshipFinished()) {
                throw new RuntimeException(msg + " Esse campeonato já terminou!");
            } else {
                throw new RuntimeException(msg + " Esse campeonato ainda não começou!");
            }
        }
        this.validateHomeAndVisitingTeams(homeTeam, visitingTeam, championship);
        return championship;
    }

    private void validateHomeAndVisitingTeams(Teams homeTeam, Teams visitingTeam, Championship championship) {
        if (this.matchesRepository.countPlayByHomeVisitingAndChampionship(homeTeam, visitingTeam, championship)) {
            throw new RuntimeException("Esse jogo já aconteceu nesse campeonato!");
        }

    }

    private Teams validTeams(Integer teamsID) {
        return this.teamsRepository.findById(teamsID).orElseThrow(() -> {
            return new RuntimeException("Time com id: " + teamsID + " não foi encontrado!");
        });
    }
}
