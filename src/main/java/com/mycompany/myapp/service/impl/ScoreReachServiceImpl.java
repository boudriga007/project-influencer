package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.ScoreReach;
import com.mycompany.myapp.repository.ScoreReachRepository;
import com.mycompany.myapp.service.ScoreReachService;
import com.mycompany.myapp.service.dto.ScoreReachDTO;
import com.mycompany.myapp.service.mapper.ScoreReachMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ScoreReach}.
 */
@Service
@Transactional
public class ScoreReachServiceImpl implements ScoreReachService {

    private static final Logger LOG = LoggerFactory.getLogger(ScoreReachServiceImpl.class);

    private final ScoreReachRepository scoreReachRepository;

    private final ScoreReachMapper scoreReachMapper;

    public ScoreReachServiceImpl(ScoreReachRepository scoreReachRepository, ScoreReachMapper scoreReachMapper) {
        this.scoreReachRepository = scoreReachRepository;
        this.scoreReachMapper = scoreReachMapper;
    }

    @Override
    public ScoreReachDTO save(ScoreReachDTO scoreReachDTO) {
        LOG.debug("Request to save ScoreReach : {}", scoreReachDTO);
        ScoreReach scoreReach = scoreReachMapper.toEntity(scoreReachDTO);
        scoreReach = scoreReachRepository.save(scoreReach);
        return scoreReachMapper.toDto(scoreReach);
    }

    @Override
    public ScoreReachDTO update(ScoreReachDTO scoreReachDTO) {
        LOG.debug("Request to update ScoreReach : {}", scoreReachDTO);
        ScoreReach scoreReach = scoreReachMapper.toEntity(scoreReachDTO);
        scoreReach = scoreReachRepository.save(scoreReach);
        return scoreReachMapper.toDto(scoreReach);
    }

    @Override
    public Optional<ScoreReachDTO> partialUpdate(ScoreReachDTO scoreReachDTO) {
        LOG.debug("Request to partially update ScoreReach : {}", scoreReachDTO);

        return scoreReachRepository
            .findById(scoreReachDTO.getId())
            .map(existingScoreReach -> {
                scoreReachMapper.partialUpdate(existingScoreReach, scoreReachDTO);

                return existingScoreReach;
            })
            .map(scoreReachRepository::save)
            .map(scoreReachMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoreReachDTO> findAll() {
        LOG.debug("Request to get all ScoreReaches");
        return scoreReachRepository.findAll().stream().map(scoreReachMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ScoreReachDTO> findOne(Long id) {
        LOG.debug("Request to get ScoreReach : {}", id);
        return scoreReachRepository.findById(id).map(scoreReachMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ScoreReach : {}", id);
        scoreReachRepository.deleteById(id);
    }
}
