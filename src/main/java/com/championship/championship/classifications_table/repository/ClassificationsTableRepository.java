package com.championship.championship.classifications_table.repository;

import com.championship.championship.championships.Championship;
import com.championship.championship.classifications_table.ClassificationsTable;
import com.championship.championship.teams.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassificationsTableRepository extends JpaRepository<ClassificationsTable, Integer> {

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM classification_table ct " +
                    "WHERE ct.team = :team " +
                    "AND ct.championship = :championship ")
    boolean findByTeamId(@Param("team") Teams team,@Param("championship") Championship championship);
}
