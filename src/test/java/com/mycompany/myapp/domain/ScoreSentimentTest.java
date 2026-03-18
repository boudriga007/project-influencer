package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.InfluenceurTestSamples.*;
import static com.mycompany.myapp.domain.ScoreSentimentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScoreSentimentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScoreSentiment.class);
        ScoreSentiment scoreSentiment1 = getScoreSentimentSample1();
        ScoreSentiment scoreSentiment2 = new ScoreSentiment();
        assertThat(scoreSentiment1).isNotEqualTo(scoreSentiment2);

        scoreSentiment2.setId(scoreSentiment1.getId());
        assertThat(scoreSentiment1).isEqualTo(scoreSentiment2);

        scoreSentiment2 = getScoreSentimentSample2();
        assertThat(scoreSentiment1).isNotEqualTo(scoreSentiment2);
    }

    @Test
    void influenceurTest() {
        ScoreSentiment scoreSentiment = getScoreSentimentRandomSampleGenerator();
        Influenceur influenceurBack = getInfluenceurRandomSampleGenerator();

        scoreSentiment.setInfluenceur(influenceurBack);
        assertThat(scoreSentiment.getInfluenceur()).isEqualTo(influenceurBack);

        scoreSentiment.influenceur(null);
        assertThat(scoreSentiment.getInfluenceur()).isNull();
    }
}
