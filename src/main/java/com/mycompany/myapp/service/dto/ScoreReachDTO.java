package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ScoreReach} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScoreReachDTO implements Serializable {

    private Long id;

    private Long nbFollowers;

    private Double engagementRate;

    private Double reachEstime;

    private Double scoreReach;

    private InfluenceurDTO influenceur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNbFollowers() {
        return nbFollowers;
    }

    public void setNbFollowers(Long nbFollowers) {
        this.nbFollowers = nbFollowers;
    }

    public Double getEngagementRate() {
        return engagementRate;
    }

    public void setEngagementRate(Double engagementRate) {
        this.engagementRate = engagementRate;
    }

    public Double getReachEstime() {
        return reachEstime;
    }

    public void setReachEstime(Double reachEstime) {
        this.reachEstime = reachEstime;
    }

    public Double getScoreReach() {
        return scoreReach;
    }

    public void setScoreReach(Double scoreReach) {
        this.scoreReach = scoreReach;
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
        if (!(o instanceof ScoreReachDTO)) {
            return false;
        }

        ScoreReachDTO scoreReachDTO = (ScoreReachDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, scoreReachDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScoreReachDTO{" +
            "id=" + getId() +
            ", nbFollowers=" + getNbFollowers() +
            ", engagementRate=" + getEngagementRate() +
            ", reachEstime=" + getReachEstime() +
            ", scoreReach=" + getScoreReach() +
            ", influenceur=" + getInfluenceur() +
            "}";
    }
}
