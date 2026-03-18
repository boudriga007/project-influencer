package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ScoreSentimentAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ScoreSentiment;
import com.mycompany.myapp.repository.ScoreSentimentRepository;
import com.mycompany.myapp.service.dto.ScoreSentimentDTO;
import com.mycompany.myapp.service.mapper.ScoreSentimentMapper;
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
 * Integration tests for the {@link ScoreSentimentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScoreSentimentResourceIT {

    private static final Double DEFAULT_RATIO_POSITIF = 1D;
    private static final Double UPDATED_RATIO_POSITIF = 2D;

    private static final Double DEFAULT_RATIO_NEGATIF = 1D;
    private static final Double UPDATED_RATIO_NEGATIF = 2D;

    private static final Double DEFAULT_RATIO_NEUTRE = 1D;
    private static final Double UPDATED_RATIO_NEUTRE = 2D;

    private static final Double DEFAULT_SCORE_SENTIMENT = 1D;
    private static final Double UPDATED_SCORE_SENTIMENT = 2D;

    private static final String ENTITY_API_URL = "/api/score-sentiments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScoreSentimentRepository scoreSentimentRepository;

    @Autowired
    private ScoreSentimentMapper scoreSentimentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScoreSentimentMockMvc;

    private ScoreSentiment scoreSentiment;

    private ScoreSentiment insertedScoreSentiment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScoreSentiment createEntity() {
        return new ScoreSentiment()
            .ratioPositif(DEFAULT_RATIO_POSITIF)
            .ratioNegatif(DEFAULT_RATIO_NEGATIF)
            .ratioNeutre(DEFAULT_RATIO_NEUTRE)
            .scoreSentiment(DEFAULT_SCORE_SENTIMENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScoreSentiment createUpdatedEntity() {
        return new ScoreSentiment()
            .ratioPositif(UPDATED_RATIO_POSITIF)
            .ratioNegatif(UPDATED_RATIO_NEGATIF)
            .ratioNeutre(UPDATED_RATIO_NEUTRE)
            .scoreSentiment(UPDATED_SCORE_SENTIMENT);
    }

    @BeforeEach
    void initTest() {
        scoreSentiment = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedScoreSentiment != null) {
            scoreSentimentRepository.delete(insertedScoreSentiment);
            insertedScoreSentiment = null;
        }
    }

    @Test
    @Transactional
    void createScoreSentiment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ScoreSentiment
        ScoreSentimentDTO scoreSentimentDTO = scoreSentimentMapper.toDto(scoreSentiment);
        var returnedScoreSentimentDTO = om.readValue(
            restScoreSentimentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scoreSentimentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ScoreSentimentDTO.class
        );

        // Validate the ScoreSentiment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedScoreSentiment = scoreSentimentMapper.toEntity(returnedScoreSentimentDTO);
        assertScoreSentimentUpdatableFieldsEquals(returnedScoreSentiment, getPersistedScoreSentiment(returnedScoreSentiment));

        insertedScoreSentiment = returnedScoreSentiment;
    }

    @Test
    @Transactional
    void createScoreSentimentWithExistingId() throws Exception {
        // Create the ScoreSentiment with an existing ID
        scoreSentiment.setId(1L);
        ScoreSentimentDTO scoreSentimentDTO = scoreSentimentMapper.toDto(scoreSentiment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScoreSentimentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scoreSentimentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ScoreSentiment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllScoreSentiments() throws Exception {
        // Initialize the database
        insertedScoreSentiment = scoreSentimentRepository.saveAndFlush(scoreSentiment);

        // Get all the scoreSentimentList
        restScoreSentimentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scoreSentiment.getId().intValue())))
            .andExpect(jsonPath("$.[*].ratioPositif").value(hasItem(DEFAULT_RATIO_POSITIF)))
            .andExpect(jsonPath("$.[*].ratioNegatif").value(hasItem(DEFAULT_RATIO_NEGATIF)))
            .andExpect(jsonPath("$.[*].ratioNeutre").value(hasItem(DEFAULT_RATIO_NEUTRE)))
            .andExpect(jsonPath("$.[*].scoreSentiment").value(hasItem(DEFAULT_SCORE_SENTIMENT)));
    }

    @Test
    @Transactional
    void getScoreSentiment() throws Exception {
        // Initialize the database
        insertedScoreSentiment = scoreSentimentRepository.saveAndFlush(scoreSentiment);

        // Get the scoreSentiment
        restScoreSentimentMockMvc
            .perform(get(ENTITY_API_URL_ID, scoreSentiment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scoreSentiment.getId().intValue()))
            .andExpect(jsonPath("$.ratioPositif").value(DEFAULT_RATIO_POSITIF))
            .andExpect(jsonPath("$.ratioNegatif").value(DEFAULT_RATIO_NEGATIF))
            .andExpect(jsonPath("$.ratioNeutre").value(DEFAULT_RATIO_NEUTRE))
            .andExpect(jsonPath("$.scoreSentiment").value(DEFAULT_SCORE_SENTIMENT));
    }

    @Test
    @Transactional
    void getNonExistingScoreSentiment() throws Exception {
        // Get the scoreSentiment
        restScoreSentimentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScoreSentiment() throws Exception {
        // Initialize the database
        insertedScoreSentiment = scoreSentimentRepository.saveAndFlush(scoreSentiment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scoreSentiment
        ScoreSentiment updatedScoreSentiment = scoreSentimentRepository.findById(scoreSentiment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedScoreSentiment are not directly saved in db
        em.detach(updatedScoreSentiment);
        updatedScoreSentiment
            .ratioPositif(UPDATED_RATIO_POSITIF)
            .ratioNegatif(UPDATED_RATIO_NEGATIF)
            .ratioNeutre(UPDATED_RATIO_NEUTRE)
            .scoreSentiment(UPDATED_SCORE_SENTIMENT);
        ScoreSentimentDTO scoreSentimentDTO = scoreSentimentMapper.toDto(updatedScoreSentiment);

        restScoreSentimentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scoreSentimentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scoreSentimentDTO))
            )
            .andExpect(status().isOk());

        // Validate the ScoreSentiment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScoreSentimentToMatchAllProperties(updatedScoreSentiment);
    }

    @Test
    @Transactional
    void putNonExistingScoreSentiment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scoreSentiment.setId(longCount.incrementAndGet());

        // Create the ScoreSentiment
        ScoreSentimentDTO scoreSentimentDTO = scoreSentimentMapper.toDto(scoreSentiment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScoreSentimentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scoreSentimentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scoreSentimentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScoreSentiment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScoreSentiment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scoreSentiment.setId(longCount.incrementAndGet());

        // Create the ScoreSentiment
        ScoreSentimentDTO scoreSentimentDTO = scoreSentimentMapper.toDto(scoreSentiment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoreSentimentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scoreSentimentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScoreSentiment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScoreSentiment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scoreSentiment.setId(longCount.incrementAndGet());

        // Create the ScoreSentiment
        ScoreSentimentDTO scoreSentimentDTO = scoreSentimentMapper.toDto(scoreSentiment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoreSentimentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scoreSentimentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScoreSentiment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScoreSentimentWithPatch() throws Exception {
        // Initialize the database
        insertedScoreSentiment = scoreSentimentRepository.saveAndFlush(scoreSentiment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scoreSentiment using partial update
        ScoreSentiment partialUpdatedScoreSentiment = new ScoreSentiment();
        partialUpdatedScoreSentiment.setId(scoreSentiment.getId());

        partialUpdatedScoreSentiment.ratioNegatif(UPDATED_RATIO_NEGATIF);

        restScoreSentimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScoreSentiment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScoreSentiment))
            )
            .andExpect(status().isOk());

        // Validate the ScoreSentiment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScoreSentimentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedScoreSentiment, scoreSentiment),
            getPersistedScoreSentiment(scoreSentiment)
        );
    }

    @Test
    @Transactional
    void fullUpdateScoreSentimentWithPatch() throws Exception {
        // Initialize the database
        insertedScoreSentiment = scoreSentimentRepository.saveAndFlush(scoreSentiment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scoreSentiment using partial update
        ScoreSentiment partialUpdatedScoreSentiment = new ScoreSentiment();
        partialUpdatedScoreSentiment.setId(scoreSentiment.getId());

        partialUpdatedScoreSentiment
            .ratioPositif(UPDATED_RATIO_POSITIF)
            .ratioNegatif(UPDATED_RATIO_NEGATIF)
            .ratioNeutre(UPDATED_RATIO_NEUTRE)
            .scoreSentiment(UPDATED_SCORE_SENTIMENT);

        restScoreSentimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScoreSentiment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScoreSentiment))
            )
            .andExpect(status().isOk());

        // Validate the ScoreSentiment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScoreSentimentUpdatableFieldsEquals(partialUpdatedScoreSentiment, getPersistedScoreSentiment(partialUpdatedScoreSentiment));
    }

    @Test
    @Transactional
    void patchNonExistingScoreSentiment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scoreSentiment.setId(longCount.incrementAndGet());

        // Create the ScoreSentiment
        ScoreSentimentDTO scoreSentimentDTO = scoreSentimentMapper.toDto(scoreSentiment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScoreSentimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scoreSentimentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scoreSentimentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScoreSentiment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScoreSentiment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scoreSentiment.setId(longCount.incrementAndGet());

        // Create the ScoreSentiment
        ScoreSentimentDTO scoreSentimentDTO = scoreSentimentMapper.toDto(scoreSentiment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoreSentimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scoreSentimentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScoreSentiment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScoreSentiment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scoreSentiment.setId(longCount.incrementAndGet());

        // Create the ScoreSentiment
        ScoreSentimentDTO scoreSentimentDTO = scoreSentimentMapper.toDto(scoreSentiment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoreSentimentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scoreSentimentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScoreSentiment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScoreSentiment() throws Exception {
        // Initialize the database
        insertedScoreSentiment = scoreSentimentRepository.saveAndFlush(scoreSentiment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the scoreSentiment
        restScoreSentimentMockMvc
            .perform(delete(ENTITY_API_URL_ID, scoreSentiment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scoreSentimentRepository.count();
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

    protected ScoreSentiment getPersistedScoreSentiment(ScoreSentiment scoreSentiment) {
        return scoreSentimentRepository.findById(scoreSentiment.getId()).orElseThrow();
    }

    protected void assertPersistedScoreSentimentToMatchAllProperties(ScoreSentiment expectedScoreSentiment) {
        assertScoreSentimentAllPropertiesEquals(expectedScoreSentiment, getPersistedScoreSentiment(expectedScoreSentiment));
    }

    protected void assertPersistedScoreSentimentToMatchUpdatableProperties(ScoreSentiment expectedScoreSentiment) {
        assertScoreSentimentAllUpdatablePropertiesEquals(expectedScoreSentiment, getPersistedScoreSentiment(expectedScoreSentiment));
    }
}
