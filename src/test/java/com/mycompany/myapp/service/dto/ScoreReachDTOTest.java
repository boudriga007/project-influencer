package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScoreReachDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScoreReachDTO.class);
        ScoreReachDTO scoreReachDTO1 = new ScoreReachDTO();
        scoreReachDTO1.setId(1L);
        ScoreReachDTO scoreReachDTO2 = new ScoreReachDTO();
        assertThat(scoreReachDTO1).isNotEqualTo(scoreReachDTO2);
        scoreReachDTO2.setId(scoreReachDTO1.getId());
        assertThat(scoreReachDTO1).isEqualTo(scoreReachDTO2);
        scoreReachDTO2.setId(2L);
        assertThat(scoreReachDTO1).isNotEqualTo(scoreReachDTO2);
        scoreReachDTO1.setId(null);
        assertThat(scoreReachDTO1).isNotEqualTo(scoreReachDTO2);
    }
}
