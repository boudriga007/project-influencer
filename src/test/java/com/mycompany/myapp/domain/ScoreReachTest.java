package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.InfluenceurTestSamples.*;
import static com.mycompany.myapp.domain.ScoreReachTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScoreReachTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScoreReach.class);
        ScoreReach scoreReach1 = getScoreReachSample1();
        ScoreReach scoreReach2 = new ScoreReach();
        assertThat(scoreReach1).isNotEqualTo(scoreReach2);

        scoreReach2.setId(scoreReach1.getId());
        assertThat(scoreReach1).isEqualTo(scoreReach2);

        scoreReach2 = getScoreReachSample2();
        assertThat(scoreReach1).isNotEqualTo(scoreReach2);
    }

    @Test
    void influenceurTest() {
        ScoreReach scoreReach = getScoreReachRandomSampleGenerator();
        Influenceur influenceurBack = getInfluenceurRandomSampleGenerator();

        scoreReach.setInfluenceur(influenceurBack);
        assertThat(scoreReach.getInfluenceur()).isEqualTo(influenceurBack);

        scoreReach.influenceur(null);
        assertThat(scoreReach.getInfluenceur()).isNull();
    }
}
