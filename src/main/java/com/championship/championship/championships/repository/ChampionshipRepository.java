package com.championship.championship.championships.repository;

import com.championship.championship.championships.Championship;
import com.championship.championship.classifications_table.ClassificationsTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChampionshipRepository extends JpaRepository<Championship, Integer> {

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM championship c " +
                    "WHERE c.name = :championshipName " +
                    "AND c.year = :championshipYear ")
    boolean countChampionshipByNameAndYear(@Param("championshipName") String championshipName,
                                           @Param("championshipYear") Integer championshipYear);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM championship c " +
                    "WHERE c.year = :year ")
    List<Championship> findAllByYear(@Param("year") int year);


}
