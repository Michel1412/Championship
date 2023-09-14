package com.championship.championship.matches.repository;

import com.championship.championship.matches.Matches;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchesRepository extends JpaRepository<Matches, Integer> {
}
