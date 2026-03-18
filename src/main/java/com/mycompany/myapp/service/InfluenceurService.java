package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.InfluenceurDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Influenceur}.
 */
public interface InfluenceurService {
    /**
     * Save a influenceur.
     *
     * @param influenceurDTO the entity to save.
     * @return the persisted entity.
     */
    InfluenceurDTO save(InfluenceurDTO influenceurDTO);

    /**
     * Updates a influenceur.
     *
     * @param influenceurDTO the entity to update.
     * @return the persisted entity.
     */
    InfluenceurDTO update(InfluenceurDTO influenceurDTO);

    /**
     * Partially updates a influenceur.
     *
     * @param influenceurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InfluenceurDTO> partialUpdate(InfluenceurDTO influenceurDTO);

    /**
     * Get all the InfluenceurDTO where ScoreReach is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<InfluenceurDTO> findAllWhereScoreReachIsNull();
    /**
     * Get all the InfluenceurDTO where ScoreSentiment is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<InfluenceurDTO> findAllWhereScoreSentimentIsNull();

    /**
     * Get the "id" influenceur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InfluenceurDTO> findOne(Long id);

    /**
     * Delete the "id" influenceur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
