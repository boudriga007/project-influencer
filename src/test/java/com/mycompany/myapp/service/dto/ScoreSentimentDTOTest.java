package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScoreSentimentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScoreSentimentDTO.class);
        ScoreSentimentDTO scoreSentimentDTO1 = new ScoreSentimentDTO();
        scoreSentimentDTO1.setId(1L);
        ScoreSentimentDTO scoreSentimentDTO2 = new ScoreSentimentDTO();
        assertThat(scoreSentimentDTO1).isNotEqualTo(scoreSentimentDTO2);
        scoreSentimentDTO2.setId(scoreSentimentDTO1.getId());
        assertThat(scoreSentimentDTO1).isEqualTo(scoreSentimentDTO2);
        scoreSentimentDTO2.setId(2L);
        assertThat(scoreSentimentDTO1).isNotEqualTo(scoreSentimentDTO2);
        scoreSentimentDTO1.setId(null);
        assertThat(scoreSentimentDTO1).isNotEqualTo(scoreSentimentDTO2);
    }
}
