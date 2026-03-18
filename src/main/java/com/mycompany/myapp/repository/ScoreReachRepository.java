package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ScoreReach;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ScoreReach entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScoreReachRepository extends JpaRepository<ScoreReach, Long> {}
