package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ScoreReachAsserts.*;
import static com.mycompany.myapp.domain.ScoreReachTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScoreReachMapperTest {

    private ScoreReachMapper scoreReachMapper;

    @BeforeEach
    void setUp() {
        scoreReachMapper = new ScoreReachMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getScoreReachSample1();
        var actual = scoreReachMapper.toEntity(scoreReachMapper.toDto(expected));
        assertScoreReachAllPropertiesEquals(expected, actual);
    }
}
