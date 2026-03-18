package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.Categorie;
import com.mycompany.myapp.domain.enumeration.Plateforme;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Influenceur} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.InfluenceurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /influenceurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InfluenceurCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Plateforme
     */
    public static class PlateformeFilter extends Filter<Plateforme> {

        public PlateformeFilter() {}

        public PlateformeFilter(PlateformeFilter filter) {
            super(filter);
        }

        @Override
        public PlateformeFilter copy() {
            return new PlateformeFilter(this);
        }
    }

    /**
     * Class for filtering Categorie
     */
    public static class CategorieFilter extends Filter<Categorie> {

        public CategorieFilter() {}

        public CategorieFilter(CategorieFilter filter) {
            super(filter);
        }

        @Override
        public CategorieFilter copy() {
            return new CategorieFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter username;

    private StringFilter photoUrl;

    private StringFilter bio;

    private PlateformeFilter plateforme;

    private CategorieFilter categorie;

    private DoubleFilter scoreGlobal;

    private LongFilter scoreReachId;

    private LongFilter scoreSentimentId;

    private Boolean distinct;

    public InfluenceurCriteria() {}

    public InfluenceurCriteria(InfluenceurCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.username = other.optionalUsername().map(StringFilter::copy).orElse(null);
        this.photoUrl = other.optionalPhotoUrl().map(StringFilter::copy).orElse(null);
        this.bio = other.optionalBio().map(StringFilter::copy).orElse(null);
        this.plateforme = other.optionalPlateforme().map(PlateformeFilter::copy).orElse(null);
        this.categorie = other.optionalCategorie().map(CategorieFilter::copy).orElse(null);
        this.scoreGlobal = other.optionalScoreGlobal().map(DoubleFilter::copy).orElse(null);
        this.scoreReachId = other.optionalScoreReachId().map(LongFilter::copy).orElse(null);
        this.scoreSentimentId = other.optionalScoreSentimentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InfluenceurCriteria copy() {
        return new InfluenceurCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNom() {
        return nom;
    }

    public Optional<StringFilter> optionalNom() {
        return Optional.ofNullable(nom);
    }

    public StringFilter nom() {
        if (nom == null) {
            setNom(new StringFilter());
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getUsername() {
        return username;
    }

    public Optional<StringFilter> optionalUsername() {
        return Optional.ofNullable(username);
    }

    public StringFilter username() {
        if (username == null) {
            setUsername(new StringFilter());
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getPhotoUrl() {
        return photoUrl;
    }

    public Optional<StringFilter> optionalPhotoUrl() {
        return Optional.ofNullable(photoUrl);
    }

    public StringFilter photoUrl() {
        if (photoUrl == null) {
            setPhotoUrl(new StringFilter());
        }
        return photoUrl;
    }

    public void setPhotoUrl(StringFilter photoUrl) {
        this.photoUrl = photoUrl;
    }

    public StringFilter getBio() {
        return bio;
    }

    public Optional<StringFilter> optionalBio() {
        return Optional.ofNullable(bio);
    }

    public StringFilter bio() {
        if (bio == null) {
            setBio(new StringFilter());
        }
        return bio;
    }

    public void setBio(StringFilter bio) {
        this.bio = bio;
    }

    public PlateformeFilter getPlateforme() {
        return plateforme;
    }

    public Optional<PlateformeFilter> optionalPlateforme() {
        return Optional.ofNullable(plateforme);
    }

    public PlateformeFilter plateforme() {
        if (plateforme == null) {
            setPlateforme(new PlateformeFilter());
        }
        return plateforme;
    }

    public void setPlateforme(PlateformeFilter plateforme) {
        this.plateforme = plateforme;
    }

    public CategorieFilter getCategorie() {
        return categorie;
    }

    public Optional<CategorieFilter> optionalCategorie() {
        return Optional.ofNullable(categorie);
    }

    public CategorieFilter categorie() {
        if (categorie == null) {
            setCategorie(new CategorieFilter());
        }
        return categorie;
    }

    public void setCategorie(CategorieFilter categorie) {
        this.categorie = categorie;
    }

    public DoubleFilter getScoreGlobal() {
        return scoreGlobal;
    }

    public Optional<DoubleFilter> optionalScoreGlobal() {
        return Optional.ofNullable(scoreGlobal);
    }

    public DoubleFilter scoreGlobal() {
        if (scoreGlobal == null) {
            setScoreGlobal(new DoubleFilter());
        }
        return scoreGlobal;
    }

    public void setScoreGlobal(DoubleFilter scoreGlobal) {
        this.scoreGlobal = scoreGlobal;
    }

    public LongFilter getScoreReachId() {
        return scoreReachId;
    }

    public Optional<LongFilter> optionalScoreReachId() {
        return Optional.ofNullable(scoreReachId);
    }

    public LongFilter scoreReachId() {
        if (scoreReachId == null) {
            setScoreReachId(new LongFilter());
        }
        return scoreReachId;
    }

    public void setScoreReachId(LongFilter scoreReachId) {
        this.scoreReachId = scoreReachId;
    }

    public LongFilter getScoreSentimentId() {
        return scoreSentimentId;
    }

    public Optional<LongFilter> optionalScoreSentimentId() {
        return Optional.ofNullable(scoreSentimentId);
    }

    public LongFilter scoreSentimentId() {
        if (scoreSentimentId == null) {
            setScoreSentimentId(new LongFilter());
        }
        return scoreSentimentId;
    }

    public void setScoreSentimentId(LongFilter scoreSentimentId) {
        this.scoreSentimentId = scoreSentimentId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InfluenceurCriteria that = (InfluenceurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(username, that.username) &&
            Objects.equals(photoUrl, that.photoUrl) &&
            Objects.equals(bio, that.bio) &&
            Objects.equals(plateforme, that.plateforme) &&
            Objects.equals(categorie, that.categorie) &&
            Objects.equals(scoreGlobal, that.scoreGlobal) &&
            Objects.equals(scoreReachId, that.scoreReachId) &&
            Objects.equals(scoreSentimentId, that.scoreSentimentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, username, photoUrl, bio, plateforme, categorie, scoreGlobal, scoreReachId, scoreSentimentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfluenceurCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalUsername().map(f -> "username=" + f + ", ").orElse("") +
            optionalPhotoUrl().map(f -> "photoUrl=" + f + ", ").orElse("") +
            optionalBio().map(f -> "bio=" + f + ", ").orElse("") +
            optionalPlateforme().map(f -> "plateforme=" + f + ", ").orElse("") +
            optionalCategorie().map(f -> "categorie=" + f + ", ").orElse("") +
            optionalScoreGlobal().map(f -> "scoreGlobal=" + f + ", ").orElse("") +
            optionalScoreReachId().map(f -> "scoreReachId=" + f + ", ").orElse("") +
            optionalScoreSentimentId().map(f -> "scoreSentimentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
