package com.championship.championship.matches.repository;

import com.championship.championship.championships.Championship;
import com.championship.championship.matches.Matches;
import com.championship.championship.teams.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;

@Repository
public interface MatchesRepository extends JpaRepository<Matches, Integer> {

    @Query (nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM matches m " +
                    "WHERE (m.match_date = :matchDate " +
                    "OR m.match_date = :matchYesterday " +
                    "OR m.match_date = :matchTomorrow) " +
                    "AND (m.home_team = :teamId " +
                    "OR m.visiting_team = :teamId) ")
    boolean findMatchDate(@Param("matchDate") Calendar matchDate, @Param("matchYesterday") Calendar matchYesterday,
                          @Param("matchTomorrow") Calendar matchTomorrow, @Param("teamId") Teams teamId);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM matches m " +
                    "WHERE m.home_team = :homeTeam " +
                    "AND m.visiting_team = :visitingTeam " +
                    "AND m.championship = :championship ")
    boolean countPlayByHomeVisitingAndChampionship(@Param("homeTeam") Teams homeTeam,@Param("visitingTeam") Teams visitingTeam,
                                                   @Param("championship") Championship championship);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) " +
                    "FROM matches m " +
                    "WHERE m.championship = :championship " +
                    "AND m.match_finish = true ")
    int countPlaysPlayedOfChampionship(@Param("championship") Championship championship);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM matches m " +
                    "WHERE m.home_team = :teamId " +
                    "OR m.visiting_team = :teamId")
    boolean findMatchById(@Param("teamId") Integer teamId);
}
