package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Influenceur;
import com.mycompany.myapp.repository.InfluenceurRepository;
import com.mycompany.myapp.service.criteria.InfluenceurCriteria;
import com.mycompany.myapp.service.dto.InfluenceurDTO;
import com.mycompany.myapp.service.mapper.InfluenceurMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Influenceur} entities in the database.
 * The main input is a {@link InfluenceurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InfluenceurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InfluenceurQueryService extends QueryService<Influenceur> {

    private static final Logger LOG = LoggerFactory.getLogger(InfluenceurQueryService.class);

    private final InfluenceurRepository influenceurRepository;

    private final InfluenceurMapper influenceurMapper;

    public InfluenceurQueryService(InfluenceurRepository influenceurRepository, InfluenceurMapper influenceurMapper) {
        this.influenceurRepository = influenceurRepository;
        this.influenceurMapper = influenceurMapper;
    }

    /**
     * Return a {@link Page} of {@link InfluenceurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InfluenceurDTO> findByCriteria(InfluenceurCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Influenceur> specification = createSpecification(criteria);
        return influenceurRepository.findAll(specification, page).map(influenceurMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InfluenceurCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Influenceur> specification = createSpecification(criteria);
        return influenceurRepository.count(specification);
    }

    /**
     * Function to convert {@link InfluenceurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Influenceur> createSpecification(InfluenceurCriteria criteria) {
        Specification<Influenceur> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Influenceur_.id),
                buildStringSpecification(criteria.getNom(), Influenceur_.nom),
                buildStringSpecification(criteria.getUsername(), Influenceur_.username),
                buildStringSpecification(criteria.getPhotoUrl(), Influenceur_.photoUrl),
                buildStringSpecification(criteria.getBio(), Influenceur_.bio),
                buildSpecification(criteria.getPlateforme(), Influenceur_.plateforme),
                buildSpecification(criteria.getCategorie(), Influenceur_.categorie),
                buildRangeSpecification(criteria.getScoreGlobal(), Influenceur_.scoreGlobal),
                buildSpecification(criteria.getScoreReachId(), root ->
                    root.join(Influenceur_.scoreReach, JoinType.LEFT).get(ScoreReach_.id)
                ),
                buildSpecification(criteria.getScoreSentimentId(), root ->
                    root.join(Influenceur_.scoreSentiment, JoinType.LEFT).get(ScoreSentiment_.id)
                )
            );
        }
        return specification;
    }
}
