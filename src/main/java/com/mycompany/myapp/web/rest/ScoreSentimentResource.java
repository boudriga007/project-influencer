package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ScoreSentimentRepository;
import com.mycompany.myapp.service.ScoreSentimentService;
import com.mycompany.myapp.service.dto.ScoreSentimentDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ScoreSentiment}.
 */
@RestController
@RequestMapping("/api/score-sentiments")
public class ScoreSentimentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ScoreSentimentResource.class);

    private static final String ENTITY_NAME = "scoreSentiment";

    @Value("${jhipster.clientApp.name:tninfluence}")
    private String applicationName;

    private final ScoreSentimentService scoreSentimentService;

    private final ScoreSentimentRepository scoreSentimentRepository;

    public ScoreSentimentResource(ScoreSentimentService scoreSentimentService, ScoreSentimentRepository scoreSentimentRepository) {
        this.scoreSentimentService = scoreSentimentService;
        this.scoreSentimentRepository = scoreSentimentRepository;
    }

    /**
     * {@code POST  /score-sentiments} : Create a new scoreSentiment.
     *
     * @param scoreSentimentDTO the scoreSentimentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scoreSentimentDTO, or with status {@code 400 (Bad Request)} if the scoreSentiment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ScoreSentimentDTO> createScoreSentiment(@RequestBody ScoreSentimentDTO scoreSentimentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ScoreSentiment : {}", scoreSentimentDTO);
        if (scoreSentimentDTO.getId() != null) {
            throw new BadRequestAlertException("A new scoreSentiment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        scoreSentimentDTO = scoreSentimentService.save(scoreSentimentDTO);
        return ResponseEntity.created(new URI("/api/score-sentiments/" + scoreSentimentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, scoreSentimentDTO.getId().toString()))
            .body(scoreSentimentDTO);
    }

    /**
     * {@code PUT  /score-sentiments/:id} : Updates an existing scoreSentiment.
     *
     * @param id the id of the scoreSentimentDTO to save.
     * @param scoreSentimentDTO the scoreSentimentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scoreSentimentDTO,
     * or with status {@code 400 (Bad Request)} if the scoreSentimentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scoreSentimentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScoreSentimentDTO> updateScoreSentiment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScoreSentimentDTO scoreSentimentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ScoreSentiment : {}, {}", id, scoreSentimentDTO);
        if (scoreSentimentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scoreSentimentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scoreSentimentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        scoreSentimentDTO = scoreSentimentService.update(scoreSentimentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scoreSentimentDTO.getId().toString()))
            .body(scoreSentimentDTO);
    }

    /**
     * {@code PATCH  /score-sentiments/:id} : Partial updates given fields of an existing scoreSentiment, field will ignore if it is null
     *
     * @param id the id of the scoreSentimentDTO to save.
     * @param scoreSentimentDTO the scoreSentimentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scoreSentimentDTO,
     * or with status {@code 400 (Bad Request)} if the scoreSentimentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the scoreSentimentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the scoreSentimentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScoreSentimentDTO> partialUpdateScoreSentiment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScoreSentimentDTO scoreSentimentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ScoreSentiment partially : {}, {}", id, scoreSentimentDTO);
        if (scoreSentimentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scoreSentimentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scoreSentimentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScoreSentimentDTO> result = scoreSentimentService.partialUpdate(scoreSentimentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scoreSentimentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /score-sentiments} : get all the Score Sentiments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Score Sentiments in body.
     */
    @GetMapping("")
    public List<ScoreSentimentDTO> getAllScoreSentiments() {
        LOG.debug("REST request to get all ScoreSentiments");
        return scoreSentimentService.findAll();
    }

    /**
     * {@code GET  /score-sentiments/:id} : get the "id" scoreSentiment.
     *
     * @param id the id of the scoreSentimentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scoreSentimentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScoreSentimentDTO> getScoreSentiment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ScoreSentiment : {}", id);
        Optional<ScoreSentimentDTO> scoreSentimentDTO = scoreSentimentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scoreSentimentDTO);
    }

    /**
     * {@code DELETE  /score-sentiments/:id} : delete the "id" scoreSentiment.
     *
     * @param id the id of the scoreSentimentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScoreSentiment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ScoreSentiment : {}", id);
        scoreSentimentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
