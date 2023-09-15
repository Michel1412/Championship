package com.championship.championship.championships.repository;

import com.championship.championship.championships.Championship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChampionshipRepository extends JpaRepository<Championship, Integer> {

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM championship c " +
                    "WHERE c.name = :championshipName " +
                    "AND c.year = :championshipYear ")
    boolean countChampionshipByNameAndYear(@Param("championshipName") String championshipName,
                                           @Param("championshipYear") Integer championshipYear);

}
