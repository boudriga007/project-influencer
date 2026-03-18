package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.Categorie;
import com.mycompany.myapp.domain.enumeration.Plateforme;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Influenceur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InfluenceurDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    private String username;

    private String photoUrl;

    private String bio;

    private Plateforme plateforme;

    private Categorie categorie;

    private Double scoreGlobal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Plateforme getPlateforme() {
        return plateforme;
    }

    public void setPlateforme(Plateforme plateforme) {
        this.plateforme = plateforme;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Double getScoreGlobal() {
        return scoreGlobal;
    }

    public void setScoreGlobal(Double scoreGlobal) {
        this.scoreGlobal = scoreGlobal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfluenceurDTO)) {
            return false;
        }

        InfluenceurDTO influenceurDTO = (InfluenceurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, influenceurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfluenceurDTO{" +
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
