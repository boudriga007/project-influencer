package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ScoreReachRepository;
import com.mycompany.myapp.service.ScoreReachService;
import com.mycompany.myapp.service.dto.ScoreReachDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ScoreReach}.
 */
@RestController
@RequestMapping("/api/score-reaches")
public class ScoreReachResource {

    private static final Logger LOG = LoggerFactory.getLogger(ScoreReachResource.class);

    private static final String ENTITY_NAME = "scoreReach";

    @Value("${jhipster.clientApp.name:tninfluence}")
    private String applicationName;

    private final ScoreReachService scoreReachService;

    private final ScoreReachRepository scoreReachRepository;

    public ScoreReachResource(ScoreReachService scoreReachService, ScoreReachRepository scoreReachRepository) {
        this.scoreReachService = scoreReachService;
        this.scoreReachRepository = scoreReachRepository;
    }

    /**
     * {@code POST  /score-reaches} : Create a new scoreReach.
     *
     * @param scoreReachDTO the scoreReachDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scoreReachDTO, or with status {@code 400 (Bad Request)} if the scoreReach has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ScoreReachDTO> createScoreReach(@RequestBody ScoreReachDTO scoreReachDTO) throws URISyntaxException {
        LOG.debug("REST request to save ScoreReach : {}", scoreReachDTO);
        if (scoreReachDTO.getId() != null) {
            throw new BadRequestAlertException("A new scoreReach cannot already have an ID", ENTITY_NAME, "idexists");
        }
        scoreReachDTO = scoreReachService.save(scoreReachDTO);
        return ResponseEntity.created(new URI("/api/score-reaches/" + scoreReachDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, scoreReachDTO.getId().toString()))
            .body(scoreReachDTO);
    }

    /**
     * {@code PUT  /score-reaches/:id} : Updates an existing scoreReach.
     *
     * @param id the id of the scoreReachDTO to save.
     * @param scoreReachDTO the scoreReachDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scoreReachDTO,
     * or with status {@code 400 (Bad Request)} if the scoreReachDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scoreReachDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScoreReachDTO> updateScoreReach(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScoreReachDTO scoreReachDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ScoreReach : {}, {}", id, scoreReachDTO);
        if (scoreReachDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scoreReachDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scoreReachRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        scoreReachDTO = scoreReachService.update(scoreReachDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scoreReachDTO.getId().toString()))
            .body(scoreReachDTO);
    }

    /**
     * {@code PATCH  /score-reaches/:id} : Partial updates given fields of an existing scoreReach, field will ignore if it is null
     *
     * @param id the id of the scoreReachDTO to save.
     * @param scoreReachDTO the scoreReachDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scoreReachDTO,
     * or with status {@code 400 (Bad Request)} if the scoreReachDTO is not valid,
     * or with status {@code 404 (Not Found)} if the scoreReachDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the scoreReachDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScoreReachDTO> partialUpdateScoreReach(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScoreReachDTO scoreReachDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ScoreReach partially : {}, {}", id, scoreReachDTO);
        if (scoreReachDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scoreReachDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scoreReachRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScoreReachDTO> result = scoreReachService.partialUpdate(scoreReachDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scoreReachDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /score-reaches} : get all the Score Reaches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Score Reaches in body.
     */
    @GetMapping("")
    public List<ScoreReachDTO> getAllScoreReaches() {
        LOG.debug("REST request to get all ScoreReaches");
        return scoreReachService.findAll();
    }

    /**
     * {@code GET  /score-reaches/:id} : get the "id" scoreReach.
     *
     * @param id the id of the scoreReachDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scoreReachDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScoreReachDTO> getScoreReach(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ScoreReach : {}", id);
        Optional<ScoreReachDTO> scoreReachDTO = scoreReachService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scoreReachDTO);
    }

    /**
     * {@code DELETE  /score-reaches/:id} : delete the "id" scoreReach.
     *
     * @param id the id of the scoreReachDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScoreReach(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ScoreReach : {}", id);
        scoreReachService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
