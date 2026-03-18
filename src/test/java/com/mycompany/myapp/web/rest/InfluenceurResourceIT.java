package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.InfluenceurAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Influenceur;
import com.mycompany.myapp.domain.enumeration.Categorie;
import com.mycompany.myapp.domain.enumeration.Plateforme;
import com.mycompany.myapp.repository.InfluenceurRepository;
import com.mycompany.myapp.service.dto.InfluenceurDTO;
import com.mycompany.myapp.service.mapper.InfluenceurMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InfluenceurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InfluenceurResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final Plateforme DEFAULT_PLATEFORME = Plateforme.INSTAGRAM;
    private static final Plateforme UPDATED_PLATEFORME = Plateforme.TIKTOK;

    private static final Categorie DEFAULT_CATEGORIE = Categorie.MODE;
    private static final Categorie UPDATED_CATEGORIE = Categorie.LIFESTYLE;

    private static final Double DEFAULT_SCORE_GLOBAL = 1D;
    private static final Double UPDATED_SCORE_GLOBAL = 2D;
    private static final Double SMALLER_SCORE_GLOBAL = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/influenceurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InfluenceurRepository influenceurRepository;

    @Autowired
    private InfluenceurMapper influenceurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInfluenceurMockMvc;

    private Influenceur influenceur;

    private Influenceur insertedInfluenceur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Influenceur createEntity() {
        return new Influenceur()
            .nom(DEFAULT_NOM)
            .username(DEFAULT_USERNAME)
            .photoUrl(DEFAULT_PHOTO_URL)
            .bio(DEFAULT_BIO)
            .plateforme(DEFAULT_PLATEFORME)
            .categorie(DEFAULT_CATEGORIE)
            .scoreGlobal(DEFAULT_SCORE_GLOBAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Influenceur createUpdatedEntity() {
        return new Influenceur()
            .nom(UPDATED_NOM)
            .username(UPDATED_USERNAME)
            .photoUrl(UPDATED_PHOTO_URL)
            .bio(UPDATED_BIO)
            .plateforme(UPDATED_PLATEFORME)
            .categorie(UPDATED_CATEGORIE)
            .scoreGlobal(UPDATED_SCORE_GLOBAL);
    }

    @BeforeEach
    void initTest() {
        influenceur = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInfluenceur != null) {
            influenceurRepository.delete(insertedInfluenceur);
            insertedInfluenceur = null;
        }
    }

    @Test
    @Transactional
    void createInfluenceur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Influenceur
        InfluenceurDTO influenceurDTO = influenceurMapper.toDto(influenceur);
        var returnedInfluenceurDTO = om.readValue(
            restInfluenceurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(influenceurDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InfluenceurDTO.class
        );

        // Validate the Influenceur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInfluenceur = influenceurMapper.toEntity(returnedInfluenceurDTO);
        assertInfluenceurUpdatableFieldsEquals(returnedInfluenceur, getPersistedInfluenceur(returnedInfluenceur));

        insertedInfluenceur = returnedInfluenceur;
    }

    @Test
    @Transactional
    void createInfluenceurWithExistingId() throws Exception {
        // Create the Influenceur with an existing ID
        influenceur.setId(1L);
        InfluenceurDTO influenceurDTO = influenceurMapper.toDto(influenceur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInfluenceurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(influenceurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Influenceur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        influenceur.setNom(null);

        // Create the Influenceur, which fails.
        InfluenceurDTO influenceurDTO = influenceurMapper.toDto(influenceur);

        restInfluenceurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(influenceurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInfluenceurs() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList
        restInfluenceurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(influenceur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)))
            .andExpect(jsonPath("$.[*].plateforme").value(hasItem(DEFAULT_PLATEFORME.toString())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE.toString())))
            .andExpect(jsonPath("$.[*].scoreGlobal").value(hasItem(DEFAULT_SCORE_GLOBAL)));
    }

    @Test
    @Transactional
    void getInfluenceur() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get the influenceur
        restInfluenceurMockMvc
            .perform(get(ENTITY_API_URL_ID, influenceur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(influenceur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.photoUrl").value(DEFAULT_PHOTO_URL))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO))
            .andExpect(jsonPath("$.plateforme").value(DEFAULT_PLATEFORME.toString()))
            .andExpect(jsonPath("$.categorie").value(DEFAULT_CATEGORIE.toString()))
            .andExpect(jsonPath("$.scoreGlobal").value(DEFAULT_SCORE_GLOBAL));
    }

    @Test
    @Transactional
    void getInfluenceursByIdFiltering() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        Long id = influenceur.getId();

        defaultInfluenceurFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInfluenceurFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInfluenceurFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInfluenceursByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where nom equals to
        defaultInfluenceurFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllInfluenceursByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where nom in
        defaultInfluenceurFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllInfluenceursByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where nom is not null
        defaultInfluenceurFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllInfluenceursByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where nom contains
        defaultInfluenceurFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllInfluenceursByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where nom does not contain
        defaultInfluenceurFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllInfluenceursByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where username equals to
        defaultInfluenceurFiltering("username.equals=" + DEFAULT_USERNAME, "username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllInfluenceursByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where username in
        defaultInfluenceurFiltering("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME, "username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllInfluenceursByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where username is not null
        defaultInfluenceurFiltering("username.specified=true", "username.specified=false");
    }

    @Test
    @Transactional
    void getAllInfluenceursByUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where username contains
        defaultInfluenceurFiltering("username.contains=" + DEFAULT_USERNAME, "username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllInfluenceursByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where username does not contain
        defaultInfluenceurFiltering("username.doesNotContain=" + UPDATED_USERNAME, "username.doesNotContain=" + DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void getAllInfluenceursByPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where photoUrl equals to
        defaultInfluenceurFiltering("photoUrl.equals=" + DEFAULT_PHOTO_URL, "photoUrl.equals=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void getAllInfluenceursByPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where photoUrl in
        defaultInfluenceurFiltering("photoUrl.in=" + DEFAULT_PHOTO_URL + "," + UPDATED_PHOTO_URL, "photoUrl.in=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void getAllInfluenceursByPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where photoUrl is not null
        defaultInfluenceurFiltering("photoUrl.specified=true", "photoUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllInfluenceursByPhotoUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where photoUrl contains
        defaultInfluenceurFiltering("photoUrl.contains=" + DEFAULT_PHOTO_URL, "photoUrl.contains=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void getAllInfluenceursByPhotoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where photoUrl does not contain
        defaultInfluenceurFiltering("photoUrl.doesNotContain=" + UPDATED_PHOTO_URL, "photoUrl.doesNotContain=" + DEFAULT_PHOTO_URL);
    }

    @Test
    @Transactional
    void getAllInfluenceursByBioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where bio equals to
        defaultInfluenceurFiltering("bio.equals=" + DEFAULT_BIO, "bio.equals=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllInfluenceursByBioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where bio in
        defaultInfluenceurFiltering("bio.in=" + DEFAULT_BIO + "," + UPDATED_BIO, "bio.in=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllInfluenceursByBioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where bio is not null
        defaultInfluenceurFiltering("bio.specified=true", "bio.specified=false");
    }

    @Test
    @Transactional
    void getAllInfluenceursByBioContainsSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where bio contains
        defaultInfluenceurFiltering("bio.contains=" + DEFAULT_BIO, "bio.contains=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllInfluenceursByBioNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where bio does not contain
        defaultInfluenceurFiltering("bio.doesNotContain=" + UPDATED_BIO, "bio.doesNotContain=" + DEFAULT_BIO);
    }

    @Test
    @Transactional
    void getAllInfluenceursByPlateformeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where plateforme equals to
        defaultInfluenceurFiltering("plateforme.equals=" + DEFAULT_PLATEFORME, "plateforme.equals=" + UPDATED_PLATEFORME);
    }

    @Test
    @Transactional
    void getAllInfluenceursByPlateformeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where plateforme in
        defaultInfluenceurFiltering(
            "plateforme.in=" + DEFAULT_PLATEFORME + "," + UPDATED_PLATEFORME,
            "plateforme.in=" + UPDATED_PLATEFORME
        );
    }

    @Test
    @Transactional
    void getAllInfluenceursByPlateformeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where plateforme is not null
        defaultInfluenceurFiltering("plateforme.specified=true", "plateforme.specified=false");
    }

    @Test
    @Transactional
    void getAllInfluenceursByCategorieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where categorie equals to
        defaultInfluenceurFiltering("categorie.equals=" + DEFAULT_CATEGORIE, "categorie.equals=" + UPDATED_CATEGORIE);
    }

    @Test
    @Transactional
    void getAllInfluenceursByCategorieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where categorie in
        defaultInfluenceurFiltering("categorie.in=" + DEFAULT_CATEGORIE + "," + UPDATED_CATEGORIE, "categorie.in=" + UPDATED_CATEGORIE);
    }

    @Test
    @Transactional
    void getAllInfluenceursByCategorieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where categorie is not null
        defaultInfluenceurFiltering("categorie.specified=true", "categorie.specified=false");
    }

    @Test
    @Transactional
    void getAllInfluenceursByScoreGlobalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where scoreGlobal equals to
        defaultInfluenceurFiltering("scoreGlobal.equals=" + DEFAULT_SCORE_GLOBAL, "scoreGlobal.equals=" + UPDATED_SCORE_GLOBAL);
    }

    @Test
    @Transactional
    void getAllInfluenceursByScoreGlobalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where scoreGlobal in
        defaultInfluenceurFiltering(
            "scoreGlobal.in=" + DEFAULT_SCORE_GLOBAL + "," + UPDATED_SCORE_GLOBAL,
            "scoreGlobal.in=" + UPDATED_SCORE_GLOBAL
        );
    }

    @Test
    @Transactional
    void getAllInfluenceursByScoreGlobalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where scoreGlobal is not null
        defaultInfluenceurFiltering("scoreGlobal.specified=true", "scoreGlobal.specified=false");
    }

    @Test
    @Transactional
    void getAllInfluenceursByScoreGlobalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where scoreGlobal is greater than or equal to
        defaultInfluenceurFiltering(
            "scoreGlobal.greaterThanOrEqual=" + DEFAULT_SCORE_GLOBAL,
            "scoreGlobal.greaterThanOrEqual=" + UPDATED_SCORE_GLOBAL
        );
    }

    @Test
    @Transactional
    void getAllInfluenceursByScoreGlobalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where scoreGlobal is less than or equal to
        defaultInfluenceurFiltering(
            "scoreGlobal.lessThanOrEqual=" + DEFAULT_SCORE_GLOBAL,
            "scoreGlobal.lessThanOrEqual=" + SMALLER_SCORE_GLOBAL
        );
    }

    @Test
    @Transactional
    void getAllInfluenceursByScoreGlobalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where scoreGlobal is less than
        defaultInfluenceurFiltering("scoreGlobal.lessThan=" + UPDATED_SCORE_GLOBAL, "scoreGlobal.lessThan=" + DEFAULT_SCORE_GLOBAL);
    }

    @Test
    @Transactional
    void getAllInfluenceursByScoreGlobalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        // Get all the influenceurList where scoreGlobal is greater than
        defaultInfluenceurFiltering("scoreGlobal.greaterThan=" + SMALLER_SCORE_GLOBAL, "scoreGlobal.greaterThan=" + DEFAULT_SCORE_GLOBAL);
    }

    private void defaultInfluenceurFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInfluenceurShouldBeFound(shouldBeFound);
        defaultInfluenceurShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInfluenceurShouldBeFound(String filter) throws Exception {
        restInfluenceurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(influenceur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)))
            .andExpect(jsonPath("$.[*].plateforme").value(hasItem(DEFAULT_PLATEFORME.toString())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE.toString())))
            .andExpect(jsonPath("$.[*].scoreGlobal").value(hasItem(DEFAULT_SCORE_GLOBAL)));

        // Check, that the count call also returns 1
        restInfluenceurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInfluenceurShouldNotBeFound(String filter) throws Exception {
        restInfluenceurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInfluenceurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInfluenceur() throws Exception {
        // Get the influenceur
        restInfluenceurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInfluenceur() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the influenceur
        Influenceur updatedInfluenceur = influenceurRepository.findById(influenceur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInfluenceur are not directly saved in db
        em.detach(updatedInfluenceur);
        updatedInfluenceur
            .nom(UPDATED_NOM)
            .username(UPDATED_USERNAME)
            .photoUrl(UPDATED_PHOTO_URL)
            .bio(UPDATED_BIO)
            .plateforme(UPDATED_PLATEFORME)
            .categorie(UPDATED_CATEGORIE)
            .scoreGlobal(UPDATED_SCORE_GLOBAL);
        InfluenceurDTO influenceurDTO = influenceurMapper.toDto(updatedInfluenceur);

        restInfluenceurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, influenceurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(influenceurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Influenceur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInfluenceurToMatchAllProperties(updatedInfluenceur);
    }

    @Test
    @Transactional
    void putNonExistingInfluenceur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        influenceur.setId(longCount.incrementAndGet());

        // Create the Influenceur
        InfluenceurDTO influenceurDTO = influenceurMapper.toDto(influenceur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfluenceurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, influenceurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(influenceurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Influenceur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInfluenceur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        influenceur.setId(longCount.incrementAndGet());

        // Create the Influenceur
        InfluenceurDTO influenceurDTO = influenceurMapper.toDto(influenceur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfluenceurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(influenceurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Influenceur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInfluenceur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        influenceur.setId(longCount.incrementAndGet());

        // Create the Influenceur
        InfluenceurDTO influenceurDTO = influenceurMapper.toDto(influenceur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfluenceurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(influenceurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Influenceur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInfluenceurWithPatch() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the influenceur using partial update
        Influenceur partialUpdatedInfluenceur = new Influenceur();
        partialUpdatedInfluenceur.setId(influenceur.getId());

        partialUpdatedInfluenceur.plateforme(UPDATED_PLATEFORME);

        restInfluenceurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfluenceur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInfluenceur))
            )
            .andExpect(status().isOk());

        // Validate the Influenceur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInfluenceurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInfluenceur, influenceur),
            getPersistedInfluenceur(influenceur)
        );
    }

    @Test
    @Transactional
    void fullUpdateInfluenceurWithPatch() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the influenceur using partial update
        Influenceur partialUpdatedInfluenceur = new Influenceur();
        partialUpdatedInfluenceur.setId(influenceur.getId());

        partialUpdatedInfluenceur
            .nom(UPDATED_NOM)
            .username(UPDATED_USERNAME)
            .photoUrl(UPDATED_PHOTO_URL)
            .bio(UPDATED_BIO)
            .plateforme(UPDATED_PLATEFORME)
            .categorie(UPDATED_CATEGORIE)
            .scoreGlobal(UPDATED_SCORE_GLOBAL);

        restInfluenceurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfluenceur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInfluenceur))
            )
            .andExpect(status().isOk());

        // Validate the Influenceur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInfluenceurUpdatableFieldsEquals(partialUpdatedInfluenceur, getPersistedInfluenceur(partialUpdatedInfluenceur));
    }

    @Test
    @Transactional
    void patchNonExistingInfluenceur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        influenceur.setId(longCount.incrementAndGet());

        // Create the Influenceur
        InfluenceurDTO influenceurDTO = influenceurMapper.toDto(influenceur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfluenceurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, influenceurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(influenceurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Influenceur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInfluenceur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        influenceur.setId(longCount.incrementAndGet());

        // Create the Influenceur
        InfluenceurDTO influenceurDTO = influenceurMapper.toDto(influenceur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfluenceurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(influenceurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Influenceur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInfluenceur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        influenceur.setId(longCount.incrementAndGet());

        // Create the Influenceur
        InfluenceurDTO influenceurDTO = influenceurMapper.toDto(influenceur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfluenceurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(influenceurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Influenceur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInfluenceur() throws Exception {
        // Initialize the database
        insertedInfluenceur = influenceurRepository.saveAndFlush(influenceur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the influenceur
        restInfluenceurMockMvc
            .perform(delete(ENTITY_API_URL_ID, influenceur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return influenceurRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Influenceur getPersistedInfluenceur(Influenceur influenceur) {
        return influenceurRepository.findById(influenceur.getId()).orElseThrow();
    }

    protected void assertPersistedInfluenceurToMatchAllProperties(Influenceur expectedInfluenceur) {
        assertInfluenceurAllPropertiesEquals(expectedInfluenceur, getPersistedInfluenceur(expectedInfluenceur));
    }

    protected void assertPersistedInfluenceurToMatchUpdatableProperties(Influenceur expectedInfluenceur) {
        assertInfluenceurAllUpdatablePropertiesEquals(expectedInfluenceur, getPersistedInfluenceur(expectedInfluenceur));
    }
}
