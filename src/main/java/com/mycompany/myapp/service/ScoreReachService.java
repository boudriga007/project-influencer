package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ScoreReachDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.ScoreReach}.
 */
public interface ScoreReachService {
    /**
     * Save a scoreReach.
     *
     * @param scoreReachDTO the entity to save.
     * @return the persisted entity.
     */
    ScoreReachDTO save(ScoreReachDTO scoreReachDTO);

    /**
     * Updates a scoreReach.
     *
     * @param scoreReachDTO the entity to update.
     * @return the persisted entity.
     */
    ScoreReachDTO update(ScoreReachDTO scoreReachDTO);

    /**
     * Partially updates a scoreReach.
     *
     * @param scoreReachDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ScoreReachDTO> partialUpdate(ScoreReachDTO scoreReachDTO);

    /**
     * Get all the scoreReaches.
     *
     * @return the list of entities.
     */
    List<ScoreReachDTO> findAll();

    /**
     * Get the "id" scoreReach.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ScoreReachDTO> findOne(Long id);

    /**
     * Delete the "id" scoreReach.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
