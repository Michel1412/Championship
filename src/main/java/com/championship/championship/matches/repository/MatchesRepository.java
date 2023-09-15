package com.championship.championship.matches.repository;

import com.championship.championship.matches.Matches;
import com.championship.championship.teams.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchesRepository extends JpaRepository<Matches, Integer> {

    @Query (nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM matches m " +
                    "WHERE m.matchDate = :matchDate " +
                    "AND m.matchDate = :matchYesterday " +
                    "AND m.matchDate = :matchTomorrow " +
                    "AND m.home_team = :teamId " +
                    "AND m.visiting_team = :teamId ")
    boolean findMatchDate(int matchDate, int matchYesterday, int matchTomorrow, Teams teamId);
}
