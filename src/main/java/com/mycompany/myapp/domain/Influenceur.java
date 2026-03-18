package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Categorie;
import com.mycompany.myapp.domain.enumeration.Plateforme;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * A Influenceur.
 */
@Entity
@Table(name = "influenceur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Influenceur implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "bio")
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "plateforme")
    private Plateforme plateforme;

    @Enumerated(EnumType.STRING)
    @Column(name = "categorie")
    private Categorie categorie;

    @Column(name = "score_global")
    private Double scoreGlobal;

    @JsonIgnoreProperties(value = { "influenceur" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "influenceur")
    private ScoreReach scoreReach;

    @JsonIgnoreProperties(value = { "influenceur" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "influenceur")
    private ScoreSentiment scoreSentiment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Influenceur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Influenceur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUsername() {
        return this.username;
    }

    public Influenceur username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public Influenceur photoUrl(String photoUrl) {
        this.setPhotoUrl(photoUrl);
        return this;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBio() {
        return this.bio;
    }

    public Influenceur bio(String bio) {
        this.setBio(bio);
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Plateforme getPlateforme() {
        return this.plateforme;
    }

    public Influenceur plateforme(Plateforme plateforme) {
        this.setPlateforme(plateforme);
        return this;
    }

    public void setPlateforme(Plateforme plateforme) {
        this.plateforme = plateforme;
    }

    public Categorie getCategorie() {
        return this.categorie;
    }

    public Influenceur categorie(Categorie categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Double getScoreGlobal() {
        return this.scoreGlobal;
    }

    public Influenceur scoreGlobal(Double scoreGlobal) {
        this.setScoreGlobal(scoreGlobal);
        return this;
    }

    public void setScoreGlobal(Double scoreGlobal) {
        this.scoreGlobal = scoreGlobal;
    }

    public ScoreReach getScoreReach() {
        return this.scoreReach;
    }

    public void setScoreReach(ScoreReach scoreReach) {
        if (this.scoreReach != null) {
            this.scoreReach.setInfluenceur(null);
        }
        if (scoreReach != null) {
            scoreReach.setInfluenceur(this);
        }
        this.scoreReach = scoreReach;
    }

    public Influenceur scoreReach(ScoreReach scoreReach) {
        this.setScoreReach(scoreReach);
        return this;
    }

    public ScoreSentiment getScoreSentiment() {
        return this.scoreSentiment;
    }

    public void setScoreSentiment(ScoreSentiment scoreSentiment) {
        if (this.scoreSentiment != null) {
            this.scoreSentiment.setInfluenceur(null);
        }
        if (scoreSentiment != null) {
            scoreSentiment.setInfluenceur(this);
        }
        this.scoreSentiment = scoreSentiment;
    }

    public Influenceur scoreSentiment(ScoreSentiment scoreSentiment) {
        this.setScoreSentiment(scoreSentiment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Influenceur)) {
            return false;
        }
        return getId() != null && getId().equals(((Influenceur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Influenceur{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", username='" + getUsername() + "'" +
            ", photoUrl='" + getPhotoUrl() + "'" +
            ", bio='" + getBio() + "'" +
            ", plateforme='" + getPlateforme() + "'" +
            ", categorie='" + getCategorie() + "'" +
            ", scoreGlobal=" + getScoreGlobal() +
            "}";
    }
}
