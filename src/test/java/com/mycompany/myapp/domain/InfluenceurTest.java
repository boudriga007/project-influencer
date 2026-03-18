package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.InfluenceurTestSamples.*;
import static com.mycompany.myapp.domain.ScoreReachTestSamples.*;
import static com.mycompany.myapp.domain.ScoreSentimentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InfluenceurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Influenceur.class);
        Influenceur influenceur1 = getInfluenceurSample1();
        Influenceur influenceur2 = new Influenceur();
        assertThat(influenceur1).isNotEqualTo(influenceur2);

        influenceur2.setId(influenceur1.getId());
        assertThat(influenceur1).isEqualTo(influenceur2);

        influenceur2 = getInfluenceurSample2();
        assertThat(influenceur1).isNotEqualTo(influenceur2);
    }

    @Test
    void scoreReachTest() {
        Influenceur influenceur = getInfluenceurRandomSampleGenerator();
        ScoreReach scoreReachBack = getScoreReachRandomSampleGenerator();

        influenceur.setScoreReach(scoreReachBack);
        assertThat(influenceur.getScoreReach()).isEqualTo(scoreReachBack);
        assertThat(scoreReachBack.getInfluenceur()).isEqualTo(influenceur);

        influenceur.scoreReach(null);
        assertThat(influenceur.getScoreReach()).isNull();
        assertThat(scoreReachBack.getInfluenceur()).isNull();
    }

    @Test
    void scoreSentimentTest() {
        Influenceur influenceur = getInfluenceurRandomSampleGenerator();
        ScoreSentiment scoreSentimentBack = getScoreSentimentRandomSampleGenerator();

        influenceur.setScoreSentiment(scoreSentimentBack);
        assertThat(influenceur.getScoreSentiment()).isEqualTo(scoreSentimentBack);
        assertThat(scoreSentimentBack.getInfluenceur()).isEqualTo(influenceur);

        influenceur.scoreSentiment(null);
        assertThat(influenceur.getScoreSentiment()).isNull();
        assertThat(scoreSentimentBack.getInfluenceur()).isNull();
    }
}
