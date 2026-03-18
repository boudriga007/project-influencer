package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * A ScoreSentiment.
 */
@Entity
@Table(name = "score_sentiment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScoreSentiment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ratio_positif")
    private Double ratioPositif;

    @Column(name = "ratio_negatif")
    private Double ratioNegatif;

    @Column(name = "ratio_neutre")
    private Double ratioNeutre;

    @Column(name = "score_sentiment")
    private Double scoreSentiment;

    @JsonIgnoreProperties(value = { "scoreReach", "scoreSentiment" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Influenceur influenceur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ScoreSentiment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRatioPositif() {
        return this.ratioPositif;
    }

    public ScoreSentiment ratioPositif(Double ratioPositif) {
        this.setRatioPositif(ratioPositif);
        return this;
    }

    public void setRatioPositif(Double ratioPositif) {
        this.ratioPositif = ratioPositif;
    }

    public Double getRatioNegatif() {
        return this.ratioNegatif;
    }

    public ScoreSentiment ratioNegatif(Double ratioNegatif) {
        this.setRatioNegatif(ratioNegatif);
        return this;
    }

    public void setRatioNegatif(Double ratioNegatif) {
        this.ratioNegatif = ratioNegatif;
    }

    public Double getRatioNeutre() {
        return this.ratioNeutre;
    }

    public ScoreSentiment ratioNeutre(Double ratioNeutre) {
        this.setRatioNeutre(ratioNeutre);
        return this;
    }

    public void setRatioNeutre(Double ratioNeutre) {
        this.ratioNeutre = ratioNeutre;
    }

    public Double getScoreSentiment() {
        return this.scoreSentiment;
    }

    public ScoreSentiment scoreSentiment(Double scoreSentiment) {
        this.setScoreSentiment(scoreSentiment);
        return this;
    }

    public void setScoreSentiment(Double scoreSentiment) {
        this.scoreSentiment = scoreSentiment;
    }

    public Influenceur getInfluenceur() {
        return this.influenceur;
    }

    public void setInfluenceur(Influenceur influenceur) {
        this.influenceur = influenceur;
    }

    public ScoreSentiment influenceur(Influenceur influenceur) {
        this.setInfluenceur(influenceur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScoreSentiment)) {
            return false;
        }
        return getId() != null && getId().equals(((ScoreSentiment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScoreSentiment{" +
            "id=" + getId() +
            ", ratioPositif=" + getRatioPositif() +
            ", ratioNegatif=" + getRatioNegatif() +
            ", ratioNeutre=" + getRatioNeutre() +
            ", scoreSentiment=" + getScoreSentiment() +
            "}";
    }
}
