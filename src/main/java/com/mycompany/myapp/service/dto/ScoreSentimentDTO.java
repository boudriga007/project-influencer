package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ScoreSentiment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScoreSentimentDTO implements Serializable {

    private Long id;

    private Double ratioPositif;

    private Double ratioNegatif;

    private Double ratioNeutre;

    private Double scoreSentiment;

    private InfluenceurDTO influenceur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRatioPositif() {
        return ratioPositif;
    }

    public void setRatioPositif(Double ratioPositif) {
        this.ratioPositif = ratioPositif;
    }

    public Double getRatioNegatif() {
        return ratioNegatif;
    }

    public void setRatioNegatif(Double ratioNegatif) {
        this.ratioNegatif = ratioNegatif;
    }

    public Double getRatioNeutre() {
        return ratioNeutre;
    }

    public void setRatioNeutre(Double ratioNeutre) {
        this.ratioNeutre = ratioNeutre;
    }

    public Double getScoreSentiment() {
        return scoreSentiment;
    }

    public void setScoreSentiment(Double scoreSentiment) {
        this.scoreSentiment = scoreSentiment;
    }

    public InfluenceurDTO getInfluenceur() {
        return influenceur;
    }

    public void setInfluenceur(InfluenceurDTO influenceur) {
        this.influenceur = influenceur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScoreSentimentDTO)) {
            return false;
        }

        ScoreSentimentDTO scoreSentimentDTO = (ScoreSentimentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, scoreSentimentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScoreSentimentDTO{" +
            "id=" + getId() +
            ", ratioPositif=" + getRatioPositif() +
            ", ratioNegatif=" + getRatioNegatif() +
            ", ratioNeutre=" + getRatioNeutre() +
            ", scoreSentiment=" + getScoreSentiment() +
            ", influenceur=" + getInfluenceur() +
            "}";
    }
}
