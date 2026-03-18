package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ScoreSentiment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ScoreSentiment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScoreSentimentRepository extends JpaRepository<ScoreSentiment, Long> {}
