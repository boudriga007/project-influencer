package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ScoreReachAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ScoreReach;
import com.mycompany.myapp.repository.ScoreReachRepository;
import com.mycompany.myapp.service.dto.ScoreReachDTO;
import com.mycompany.myapp.service.mapper.ScoreReachMapper;
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
 * Integration tests for the {@link ScoreReachResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScoreReachResourceIT {

    private static final Long DEFAULT_NB_FOLLOWERS = 1L;
    private static final Long UPDATED_NB_FOLLOWERS = 2L;

    private static final Double DEFAULT_ENGAGEMENT_RATE = 1D;
    private static final Double UPDATED_ENGAGEMENT_RATE = 2D;

    private static final Double DEFAULT_REACH_ESTIME = 1D;
    private static final Double UPDATED_REACH_ESTIME = 2D;

    private static final Double DEFAULT_SCORE_REACH = 1D;
    private static final Double UPDATED_SCORE_REACH = 2D;

    private static final String ENTITY_API_URL = "/api/score-reaches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScoreReachRepository scoreReachRepository;

    @Autowired
    private ScoreReachMapper scoreReachMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScoreReachMockMvc;

    private ScoreReach scoreReach;

    private ScoreReach insertedScoreReach;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScoreReach createEntity() {
        return new ScoreReach()
            .nbFollowers(DEFAULT_NB_FOLLOWERS)
            .engagementRate(DEFAULT_ENGAGEMENT_RATE)
            .reachEstime(DEFAULT_REACH_ESTIME)
            .scoreReach(DEFAULT_SCORE_REACH);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScoreReach createUpdatedEntity() {
        return new ScoreReach()
            .nbFollowers(UPDATED_NB_FOLLOWERS)
            .engagementRate(UPDATED_ENGAGEMENT_RATE)
            .reachEstime(UPDATED_REACH_ESTIME)
            .scoreReach(UPDATED_SCORE_REACH);
    }

    @BeforeEach
    void initTest() {
        scoreReach = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedScoreReach != null) {
            scoreReachRepository.delete(insertedScoreReach);
            insertedScoreReach = null;
        }
    }

    @Test
    @Transactional
    void createScoreReach() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ScoreReach
        ScoreReachDTO scoreReachDTO = scoreReachMapper.toDto(scoreReach);
        var returnedScoreReachDTO = om.readValue(
            restScoreReachMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scoreReachDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ScoreReachDTO.class
        );

        // Validate the ScoreReach in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedScoreReach = scoreReachMapper.toEntity(returnedScoreReachDTO);
        assertScoreReachUpdatableFieldsEquals(returnedScoreReach, getPersistedScoreReach(returnedScoreReach));

        insertedScoreReach = returnedScoreReach;
    }

    @Test
    @Transactional
    void createScoreReachWithExistingId() throws Exception {
        // Create the ScoreReach with an existing ID
        scoreReach.setId(1L);
        ScoreReachDTO scoreReachDTO = scoreReachMapper.toDto(scoreReach);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScoreReachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scoreReachDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ScoreReach in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllScoreReaches() throws Exception {
        // Initialize the database
        insertedScoreReach = scoreReachRepository.saveAndFlush(scoreReach);

        // Get all the scoreReachList
        restScoreReachMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scoreReach.getId().intValue())))
            .andExpect(jsonPath("$.[*].nbFollowers").value(hasItem(DEFAULT_NB_FOLLOWERS.intValue())))
            .andExpect(jsonPath("$.[*].engagementRate").value(hasItem(DEFAULT_ENGAGEMENT_RATE)))
            .andExpect(jsonPath("$.[*].reachEstime").value(hasItem(DEFAULT_REACH_ESTIME)))
            .andExpect(jsonPath("$.[*].scoreReach").value(hasItem(DEFAULT_SCORE_REACH)));
    }

    @Test
    @Transactional
    void getScoreReach() throws Exception {
        // Initialize the database
        insertedScoreReach = scoreReachRepository.saveAndFlush(scoreReach);

        // Get the scoreReach
        restScoreReachMockMvc
            .perform(get(ENTITY_API_URL_ID, scoreReach.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scoreReach.getId().intValue()))
            .andExpect(jsonPath("$.nbFollowers").value(DEFAULT_NB_FOLLOWERS.intValue()))
            .andExpect(jsonPath("$.engagementRate").value(DEFAULT_ENGAGEMENT_RATE))
            .andExpect(jsonPath("$.reachEstime").value(DEFAULT_REACH_ESTIME))
            .andExpect(jsonPath("$.scoreReach").value(DEFAULT_SCORE_REACH));
    }

    @Test
    @Transactional
    void getNonExistingScoreReach() throws Exception {
        // Get the scoreReach
        restScoreReachMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScoreReach() throws Exception {
        // Initialize the database
        insertedScoreReach = scoreReachRepository.saveAndFlush(scoreReach);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scoreReach
        ScoreReach updatedScoreReach = scoreReachRepository.findById(scoreReach.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedScoreReach are not directly saved in db
        em.detach(updatedScoreReach);
        updatedScoreReach
            .nbFollowers(UPDATED_NB_FOLLOWERS)
            .engagementRate(UPDATED_ENGAGEMENT_RATE)
            .reachEstime(UPDATED_REACH_ESTIME)
            .scoreReach(UPDATED_SCORE_REACH);
        ScoreReachDTO scoreReachDTO = scoreReachMapper.toDto(updatedScoreReach);

        restScoreReachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scoreReachDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scoreReachDTO))
            )
            .andExpect(status().isOk());

        // Validate the ScoreReach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScoreReachToMatchAllProperties(updatedScoreReach);
    }

    @Test
    @Transactional
    void putNonExistingScoreReach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scoreReach.setId(longCount.incrementAndGet());

        // Create the ScoreReach
        ScoreReachDTO scoreReachDTO = scoreReachMapper.toDto(scoreReach);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScoreReachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scoreReachDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scoreReachDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScoreReach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScoreReach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scoreReach.setId(longCount.incrementAndGet());

        // Create the ScoreReach
        ScoreReachDTO scoreReachDTO = scoreReachMapper.toDto(scoreReach);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoreReachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scoreReachDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScoreReach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScoreReach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scoreReach.setId(longCount.incrementAndGet());

        // Create the ScoreReach
        ScoreReachDTO scoreReachDTO = scoreReachMapper.toDto(scoreReach);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoreReachMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scoreReachDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScoreReach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScoreReachWithPatch() throws Exception {
        // Initialize the database
        insertedScoreReach = scoreReachRepository.saveAndFlush(scoreReach);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scoreReach using partial update
        ScoreReach partialUpdatedScoreReach = new ScoreReach();
        partialUpdatedScoreReach.setId(scoreReach.getId());

        partialUpdatedScoreReach.nbFollowers(UPDATED_NB_FOLLOWERS).reachEstime(UPDATED_REACH_ESTIME);

        restScoreReachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScoreReach.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScoreReach))
            )
            .andExpect(status().isOk());

        // Validate the ScoreReach in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScoreReachUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedScoreReach, scoreReach),
            getPersistedScoreReach(scoreReach)
        );
    }

    @Test
    @Transactional
    void fullUpdateScoreReachWithPatch() throws Exception {
        // Initialize the database
        insertedScoreReach = scoreReachRepository.saveAndFlush(scoreReach);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scoreReach using partial update
        ScoreReach partialUpdatedScoreReach = new ScoreReach();
        partialUpdatedScoreReach.setId(scoreReach.getId());

        partialUpdatedScoreReach
            .nbFollowers(UPDATED_NB_FOLLOWERS)
            .engagementRate(UPDATED_ENGAGEMENT_RATE)
            .reachEstime(UPDATED_REACH_ESTIME)
            .scoreReach(UPDATED_SCORE_REACH);

        restScoreReachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScoreReach.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScoreReach))
            )
            .andExpect(status().isOk());

        // Validate the ScoreReach in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScoreReachUpdatableFieldsEquals(partialUpdatedScoreReach, getPersistedScoreReach(partialUpdatedScoreReach));
    }

    @Test
    @Transactional
    void patchNonExistingScoreReach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scoreReach.setId(longCount.incrementAndGet());

        // Create the ScoreReach
        ScoreReachDTO scoreReachDTO = scoreReachMapper.toDto(scoreReach);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScoreReachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scoreReachDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scoreReachDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScoreReach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScoreReach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scoreReach.setId(longCount.incrementAndGet());

        // Create the ScoreReach
        ScoreReachDTO scoreReachDTO = scoreReachMapper.toDto(scoreReach);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoreReachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scoreReachDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScoreReach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScoreReach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scoreReach.setId(longCount.incrementAndGet());

        // Create the ScoreReach
        ScoreReachDTO scoreReachDTO = scoreReachMapper.toDto(scoreReach);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoreReachMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scoreReachDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScoreReach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScoreReach() throws Exception {
        // Initialize the database
        insertedScoreReach = scoreReachRepository.saveAndFlush(scoreReach);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the scoreReach
        restScoreReachMockMvc
            .perform(delete(ENTITY_API_URL_ID, scoreReach.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scoreReachRepository.count();
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

    protected ScoreReach getPersistedScoreReach(ScoreReach scoreReach) {
        return scoreReachRepository.findById(scoreReach.getId()).orElseThrow();
    }

    protected void assertPersistedScoreReachToMatchAllProperties(ScoreReach expectedScoreReach) {
        assertScoreReachAllPropertiesEquals(expectedScoreReach, getPersistedScoreReach(expectedScoreReach));
    }

    protected void assertPersistedScoreReachToMatchUpdatableProperties(ScoreReach expectedScoreReach) {
        assertScoreReachAllUpdatablePropertiesEquals(expectedScoreReach, getPersistedScoreReach(expectedScoreReach));
    }
}
