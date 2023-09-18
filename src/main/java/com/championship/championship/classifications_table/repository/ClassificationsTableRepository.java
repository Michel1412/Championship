package com.championship.championship.classifications_table.repository;

import com.championship.championship.classifications_table.ClassificationsTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassificationsTableRepository extends JpaRepository<ClassificationsTable, Integer> {

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM classification_table ct " +
                    "WHERE ct.team = :teamId " +
                    "AND ct.championship = :championshipId ")
    boolean findByTeamIdAndChampionship(@Param("teamId") Integer teamId, @Param("championshipId") Integer championshipId);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) " +
                    "FROM classification_table ct " +
                    "WHERE ct.championship = :championshipId ")
    int countTeamsOfChampionship(@Param("championshipId") Integer championshipId);
    @Query(nativeQuery = true,
            value = "SELECT ct.id " +
                    "FROM classification_table ct " +
                    "WHERE ct.team = :teamId " +
                    "AND ct.championship = :championshipId ")
    Integer pickByTeamIdAndChampionship(@Param("teamId") Integer id,@Param("championshipId") Integer championshipId);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM classification_table ct " +
                    "WHERE ct.team = :teamId ")
    boolean countTeamsOnTablesById(@Param("teamId") Integer id);
}
