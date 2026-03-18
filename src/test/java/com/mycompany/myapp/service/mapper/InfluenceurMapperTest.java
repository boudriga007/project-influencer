package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.InfluenceurAsserts.*;
import static com.mycompany.myapp.domain.InfluenceurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InfluenceurMapperTest {

    private InfluenceurMapper influenceurMapper;

    @BeforeEach
    void setUp() {
        influenceurMapper = new InfluenceurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInfluenceurSample1();
        var actual = influenceurMapper.toEntity(influenceurMapper.toDto(expected));
        assertInfluenceurAllPropertiesEquals(expected, actual);
    }
}
