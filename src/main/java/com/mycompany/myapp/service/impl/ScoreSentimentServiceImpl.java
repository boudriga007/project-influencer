package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.ScoreSentiment;
import com.mycompany.myapp.repository.ScoreSentimentRepository;
import com.mycompany.myapp.service.ScoreSentimentService;
import com.mycompany.myapp.service.dto.ScoreSentimentDTO;
import com.mycompany.myapp.service.mapper.ScoreSentimentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ScoreSentiment}.
 */
@Service
@Transactional
public class ScoreSentimentServiceImpl implements ScoreSentimentService {

    private static final Logger LOG = LoggerFactory.getLogger(ScoreSentimentServiceImpl.class);

    private final ScoreSentimentRepository scoreSentimentRepository;

    private final ScoreSentimentMapper scoreSentimentMapper;

    public ScoreSentimentServiceImpl(ScoreSentimentRepository scoreSentimentRepository, ScoreSentimentMapper scoreSentimentMapper) {
        this.scoreSentimentRepository = scoreSentimentRepository;
        this.scoreSentimentMapper = scoreSentimentMapper;
    }

    @Override
    public ScoreSentimentDTO save(ScoreSentimentDTO scoreSentimentDTO) {
        LOG.debug("Request to save ScoreSentiment : {}", scoreSentimentDTO);
        ScoreSentiment scoreSentiment = scoreSentimentMapper.toEntity(scoreSentimentDTO);
        scoreSentiment = scoreSentimentRepository.save(scoreSentiment);
        return scoreSentimentMapper.toDto(scoreSentiment);
    }

    @Override
    public ScoreSentimentDTO update(ScoreSentimentDTO scoreSentimentDTO) {
        LOG.debug("Request to update ScoreSentiment : {}", scoreSentimentDTO);
        ScoreSentiment scoreSentiment = scoreSentimentMapper.toEntity(scoreSentimentDTO);
        scoreSentiment = scoreSentimentRepository.save(scoreSentiment);
        return scoreSentimentMapper.toDto(scoreSentiment);
    }

    @Override
    public Optional<ScoreSentimentDTO> partialUpdate(ScoreSentimentDTO scoreSentimentDTO) {
        LOG.debug("Request to partially update ScoreSentiment : {}", scoreSentimentDTO);

        return scoreSentimentRepository
            .findById(scoreSentimentDTO.getId())
            .map(existingScoreSentiment -> {
                scoreSentimentMapper.partialUpdate(existingScoreSentiment, scoreSentimentDTO);

                return existingScoreSentiment;
            })
            .map(scoreSentimentRepository::save)
            .map(scoreSentimentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoreSentimentDTO> findAll() {
        LOG.debug("Request to get all ScoreSentiments");
        return scoreSentimentRepository
            .findAll()
            .stream()
            .map(scoreSentimentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ScoreSentimentDTO> findOne(Long id) {
        LOG.debug("Request to get ScoreSentiment : {}", id);
        return scoreSentimentRepository.findById(id).map(scoreSentimentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ScoreSentiment : {}", id);
        scoreSentimentRepository.deleteById(id);
    }
}
