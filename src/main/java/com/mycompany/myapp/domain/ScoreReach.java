package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * A ScoreReach.
 */
@Entity
@Table(name = "score_reach")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScoreReach implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nb_followers")
    private Long nbFollowers;

    @Column(name = "engagement_rate")
    private Double engagementRate;

    @Column(name = "reach_estime")
    private Double reachEstime;

    @Column(name = "score_reach")
    private Double scoreReach;

    @JsonIgnoreProperties(value = { "scoreReach", "scoreSentiment" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Influenceur influenceur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ScoreReach id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNbFollowers() {
        return this.nbFollowers;
    }

    public ScoreReach nbFollowers(Long nbFollowers) {
        this.setNbFollowers(nbFollowers);
        return this;
    }

    public void setNbFollowers(Long nbFollowers) {
        this.nbFollowers = nbFollowers;
    }

    public Double getEngagementRate() {
        return this.engagementRate;
    }

    public ScoreReach engagementRate(Double engagementRate) {
        this.setEngagementRate(engagementRate);
        return this;
    }

    public void setEngagementRate(Double engagementRate) {
        this.engagementRate = engagementRate;
    }

    public Double getReachEstime() {
        return this.reachEstime;
    }

    public ScoreReach reachEstime(Double reachEstime) {
        this.setReachEstime(reachEstime);
        return this;
    }

    public void setReachEstime(Double reachEstime) {
        this.reachEstime = reachEstime;
    }

    public Double getScoreReach() {
        return this.scoreReach;
    }

    public ScoreReach scoreReach(Double scoreReach) {
        this.setScoreReach(scoreReach);
        return this;
    }

    public void setScoreReach(Double scoreReach) {
        this.scoreReach = scoreReach;
    }

    public Influenceur getInfluenceur() {
        return this.influenceur;
    }

    public void setInfluenceur(Influenceur influenceur) {
        this.influenceur = influenceur;
    }

    public ScoreReach influenceur(Influenceur influenceur) {
        this.setInfluenceur(influenceur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScoreReach)) {
            return false;
        }
        return getId() != null && getId().equals(((ScoreReach) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScoreReach{" +
            "id=" + getId() +
            ", nbFollowers=" + getNbFollowers() +
            ", engagementRate=" + getEngagementRate() +
            ", reachEstime=" + getReachEstime() +
            ", scoreReach=" + getScoreReach() +
            "}";
    }
}
