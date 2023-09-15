package com.championship.championship.matches.service;

import com.championship.championship.championships.Championship;
import com.championship.championship.championships.repository.ChampionshipRepository;
import com.championship.championship.matches.Matches;
import com.championship.championship.matches.matchesDTO.MatchesDTO;
import com.championship.championship.matches.repository.MatchesRepository;
import com.championship.championship.teams.Teams;
import com.championship.championship.teams.repository.TeamsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Service
public class MatchesService {

    private final MatchesRepository matchesRepository;
    private final TeamsRepository teamsRepository;
    private final ChampionshipRepository championshipRepository;

    public MatchesService(MatchesRepository matchesRepository, TeamsRepository teamsRepository, ChampionshipRepository championshipRepository) {
        this.matchesRepository = matchesRepository;
        this.teamsRepository = teamsRepository;
        this.championshipRepository = championshipRepository;
    }

    public Object createMatch(MatchesDTO matchesDTO) {
        Matches matches = this.matchToSave(matchesDTO);
        return ResponseEntity.ok(this.matchesRepository.save(matches));
    }

    private Matches matchToSave(MatchesDTO matchesDTO) {
        Matches matches = new Matches();
        Teams homeTeam = this.validTeams(matchesDTO.getHomeTeamId());
        Teams visitingTeam = this.validTeams(matchesDTO.getVisitingTeamId());
        this.validateTeamsCanPlay(homeTeam, visitingTeam, matchesDTO.getMatchDate());
        if (Objects.nonNull(matchesDTO.getChampionshipId())) {
            this.validChampionshipId(matchesDTO.getChampionshipId());
        }
        matches.setHomeTeam(homeTeam);
        matches.setVisitingTeam(visitingTeam);
        return matches;
    }

    private void validateTeamsCanPlay(Teams homeTeam, Teams visitingTeam, Calendar matchDate) {
        Calendar minimusDate = Calendar.getInstance();
        String msg = "";
        if ((minimusDate.get(5) + 3) < matchDate.get(5) && minimusDate.get(3) == matchDate.get(3)) {
            msg = "Muito proximo do dia de hoje!";
        }
        if (this.matchesRepository.findMatchDate(matchDate.get(4),(matchDate.get(4)+1),(matchDate.get(4)-1), homeTeam)) {
            msg = "Muito da data de outo jogo do time da casa!";
        }
        if (this.matchesRepository.findMatchDate(matchDate.get(4),(matchDate.get(4)+1),(matchDate.get(4)-1), visitingTeam)) {
            msg = "Muito da data de outo jogo do time visitante!";
        }
        if (msg.isEmpty()) {
            throw new RuntimeException("Não foi possivel marcar o jogo. " + msg);
        }
        if (homeTeam.equals(visitingTeam)) {
            throw new RuntimeException("Não é possivel marcar jogos entre times iguais!");
        }

    }

    private void validChampionshipId(Integer championshipId) {
        Championship championship = this.championshipRepository.findById(championshipId).orElseThrow(() -> {
            return new RuntimeException("Campeonato não encontrado!");
        });
        if (championship.isChampionshipStarted() || !championship.isChampionshipFinished()) {
            String msg = "Não é possível marcar jogos nesse campeonato!";
            if (championship.isChampionshipFinished()) {
                throw new RuntimeException(msg + " Esse campeonato já terminou!");
            } else {
                throw new RuntimeException(msg + " Esse campeonato ainda não começou!");
            }
        }
    }

    private Teams validTeams(Integer teamsID) {
        return this.teamsRepository.findById(teamsID).orElseThrow(() -> {
            return new RuntimeException("Time com id: " + teamsID + " não foi encontrado!");
        });
    }
}
