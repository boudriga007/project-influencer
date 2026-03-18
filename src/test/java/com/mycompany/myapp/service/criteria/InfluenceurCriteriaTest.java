package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InfluenceurCriteriaTest {

    @Test
    void newInfluenceurCriteriaHasAllFiltersNullTest() {
        var influenceurCriteria = new InfluenceurCriteria();
        assertThat(influenceurCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void influenceurCriteriaFluentMethodsCreatesFiltersTest() {
        var influenceurCriteria = new InfluenceurCriteria();

        setAllFilters(influenceurCriteria);

        assertThat(influenceurCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void influenceurCriteriaCopyCreatesNullFilterTest() {
        var influenceurCriteria = new InfluenceurCriteria();
        var copy = influenceurCriteria.copy();

        assertThat(influenceurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(influenceurCriteria)
        );
    }

    @Test
    void influenceurCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var influenceurCriteria = new InfluenceurCriteria();
        setAllFilters(influenceurCriteria);

        var copy = influenceurCriteria.copy();

        assertThat(influenceurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(influenceurCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var influenceurCriteria = new InfluenceurCriteria();

        assertThat(influenceurCriteria).hasToString("InfluenceurCriteria{}");
    }

    private static void setAllFilters(InfluenceurCriteria influenceurCriteria) {
        influenceurCriteria.id();
        influenceurCriteria.nom();
        influenceurCriteria.username();
        influenceurCriteria.photoUrl();
        influenceurCriteria.bio();
        influenceurCriteria.plateforme();
        influenceurCriteria.categorie();
        influenceurCriteria.scoreGlobal();
        influenceurCriteria.scoreReachId();
        influenceurCriteria.scoreSentimentId();
        influenceurCriteria.distinct();
    }

    private static Condition<InfluenceurCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getUsername()) &&
                condition.apply(criteria.getPhotoUrl()) &&
                condition.apply(criteria.getBio()) &&
                condition.apply(criteria.getPlateforme()) &&
                condition.apply(criteria.getCategorie()) &&
                condition.apply(criteria.getScoreGlobal()) &&
                condition.apply(criteria.getScoreReachId()) &&
                condition.apply(criteria.getScoreSentimentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InfluenceurCriteria> copyFiltersAre(InfluenceurCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getUsername(), copy.getUsername()) &&
                condition.apply(criteria.getPhotoUrl(), copy.getPhotoUrl()) &&
                condition.apply(criteria.getBio(), copy.getBio()) &&
                condition.apply(criteria.getPlateforme(), copy.getPlateforme()) &&
                condition.apply(criteria.getCategorie(), copy.getCategorie()) &&
                condition.apply(criteria.getScoreGlobal(), copy.getScoreGlobal()) &&
                condition.apply(criteria.getScoreReachId(), copy.getScoreReachId()) &&
                condition.apply(criteria.getScoreSentimentId(), copy.getScoreSentimentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
