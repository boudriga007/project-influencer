package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ScoreSentimentAsserts.*;
import static com.mycompany.myapp.domain.ScoreSentimentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScoreSentimentMapperTest {

    private ScoreSentimentMapper scoreSentimentMapper;

    @BeforeEach
    void setUp() {
        scoreSentimentMapper = new ScoreSentimentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getScoreSentimentSample1();
        var actual = scoreSentimentMapper.toEntity(scoreSentimentMapper.toDto(expected));
        assertScoreSentimentAllPropertiesEquals(expected, actual);
    }
}
