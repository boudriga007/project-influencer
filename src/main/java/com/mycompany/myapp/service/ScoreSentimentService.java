package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ScoreSentimentDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.ScoreSentiment}.
 */
public interface ScoreSentimentService {
    /**
     * Save a scoreSentiment.
     *
     * @param scoreSentimentDTO the entity to save.
     * @return the persisted entity.
     */
    ScoreSentimentDTO save(ScoreSentimentDTO scoreSentimentDTO);

    /**
     * Updates a scoreSentiment.
     *
     * @param scoreSentimentDTO the entity to update.
     * @return the persisted entity.
     */
    ScoreSentimentDTO update(ScoreSentimentDTO scoreSentimentDTO);

    /**
     * Partially updates a scoreSentiment.
     *
     * @param scoreSentimentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ScoreSentimentDTO> partialUpdate(ScoreSentimentDTO scoreSentimentDTO);

    /**
     * Get all the scoreSentiments.
     *
     * @return the list of entities.
     */
    List<ScoreSentimentDTO> findAll();

    /**
     * Get the "id" scoreSentiment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ScoreSentimentDTO> findOne(Long id);

    /**
     * Delete the "id" scoreSentiment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
