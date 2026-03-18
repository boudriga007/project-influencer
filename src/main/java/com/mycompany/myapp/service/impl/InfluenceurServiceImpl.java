package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Influenceur;
import com.mycompany.myapp.repository.InfluenceurRepository;
import com.mycompany.myapp.service.InfluenceurService;
import com.mycompany.myapp.service.dto.InfluenceurDTO;
import com.mycompany.myapp.service.mapper.InfluenceurMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Influenceur}.
 */
@Service
@Transactional
public class InfluenceurServiceImpl implements InfluenceurService {

    private static final Logger LOG = LoggerFactory.getLogger(InfluenceurServiceImpl.class);

    private final InfluenceurRepository influenceurRepository;

    private final InfluenceurMapper influenceurMapper;

    public InfluenceurServiceImpl(InfluenceurRepository influenceurRepository, InfluenceurMapper influenceurMapper) {
        this.influenceurRepository = influenceurRepository;
        this.influenceurMapper = influenceurMapper;
    }

    @Override
    public InfluenceurDTO save(InfluenceurDTO influenceurDTO) {
        LOG.debug("Request to save Influenceur : {}", influenceurDTO);
        Influenceur influenceur = influenceurMapper.toEntity(influenceurDTO);
        influenceur = influenceurRepository.save(influenceur);
        return influenceurMapper.toDto(influenceur);
    }

    @Override
    public InfluenceurDTO update(InfluenceurDTO influenceurDTO) {
        LOG.debug("Request to update Influenceur : {}", influenceurDTO);
        Influenceur influenceur = influenceurMapper.toEntity(influenceurDTO);
        influenceur = influenceurRepository.save(influenceur);
        return influenceurMapper.toDto(influenceur);
    }

    @Override
    public Optional<InfluenceurDTO> partialUpdate(InfluenceurDTO influenceurDTO) {
        LOG.debug("Request to partially update Influenceur : {}", influenceurDTO);

        return influenceurRepository
            .findById(influenceurDTO.getId())
            .map(existingInfluenceur -> {
                influenceurMapper.partialUpdate(existingInfluenceur, influenceurDTO);

                return existingInfluenceur;
            })
            .map(influenceurRepository::save)
            .map(influenceurMapper::toDto);
    }

    /**
     *  Get all the influenceurs where ScoreReach is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InfluenceurDTO> findAllWhereScoreReachIsNull() {
        LOG.debug("Request to get all influenceurs where ScoreReach is null");
        return StreamSupport.stream(influenceurRepository.findAll().spliterator(), false)
            .filter(influenceur -> influenceur.getScoreReach() == null)
            .map(influenceurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the influenceurs where ScoreSentiment is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InfluenceurDTO> findAllWhereScoreSentimentIsNull() {
        LOG.debug("Request to get all influenceurs where ScoreSentiment is null");
        return StreamSupport.stream(influenceurRepository.findAll().spliterator(), false)
            .filter(influenceur -> influenceur.getScoreSentiment() == null)
            .map(influenceurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InfluenceurDTO> findOne(Long id) {
        LOG.debug("Request to get Influenceur : {}", id);
        return influenceurRepository.findById(id).map(influenceurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Influenceur : {}", id);
        influenceurRepository.deleteById(id);
    }
}
