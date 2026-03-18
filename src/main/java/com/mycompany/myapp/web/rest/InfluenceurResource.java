package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.InfluenceurRepository;
import com.mycompany.myapp.service.InfluenceurQueryService;
import com.mycompany.myapp.service.InfluenceurService;
import com.mycompany.myapp.service.criteria.InfluenceurCriteria;
import com.mycompany.myapp.service.dto.InfluenceurDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Influenceur}.
 */
@RestController
@RequestMapping("/api/influenceurs")
public class InfluenceurResource {

    private static final Logger LOG = LoggerFactory.getLogger(InfluenceurResource.class);

    private static final String ENTITY_NAME = "influenceur";

    @Value("${jhipster.clientApp.name:tninfluence}")
    private String applicationName;

    private final InfluenceurService influenceurService;

    private final InfluenceurRepository influenceurRepository;

    private final InfluenceurQueryService influenceurQueryService;

    public InfluenceurResource(
        InfluenceurService influenceurService,
        InfluenceurRepository influenceurRepository,
        InfluenceurQueryService influenceurQueryService
    ) {
        this.influenceurService = influenceurService;
        this.influenceurRepository = influenceurRepository;
        this.influenceurQueryService = influenceurQueryService;
    }

    /**
     * {@code POST  /influenceurs} : Create a new influenceur.
     *
     * @param influenceurDTO the influenceurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new influenceurDTO, or with status {@code 400 (Bad Request)} if the influenceur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InfluenceurDTO> createInfluenceur(@Valid @RequestBody InfluenceurDTO influenceurDTO) throws URISyntaxException {
        LOG.debug("REST request to save Influenceur : {}", influenceurDTO);
        if (influenceurDTO.getId() != null) {
            throw new BadRequestAlertException("A new influenceur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        influenceurDTO = influenceurService.save(influenceurDTO);
        return ResponseEntity.created(new URI("/api/influenceurs/" + influenceurDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, influenceurDTO.getId().toString()))
            .body(influenceurDTO);
    }

    /**
     * {@code PUT  /influenceurs/:id} : Updates an existing influenceur.
     *
     * @param id the id of the influenceurDTO to save.
     * @param influenceurDTO the influenceurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated influenceurDTO,
     * or with status {@code 400 (Bad Request)} if the influenceurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the influenceurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InfluenceurDTO> updateInfluenceur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InfluenceurDTO influenceurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Influenceur : {}, {}", id, influenceurDTO);
        if (influenceurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, influenceurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!influenceurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        influenceurDTO = influenceurService.update(influenceurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, influenceurDTO.getId().toString()))
            .body(influenceurDTO);
    }

    /**
     * {@code PATCH  /influenceurs/:id} : Partial updates given fields of an existing influenceur, field will ignore if it is null
     *
     * @param id the id of the influenceurDTO to save.
     * @param influenceurDTO the influenceurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated influenceurDTO,
     * or with status {@code 400 (Bad Request)} if the influenceurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the influenceurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the influenceurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InfluenceurDTO> partialUpdateInfluenceur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InfluenceurDTO influenceurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Influenceur partially : {}, {}", id, influenceurDTO);
        if (influenceurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, influenceurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!influenceurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InfluenceurDTO> result = influenceurService.partialUpdate(influenceurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, influenceurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /influenceurs} : get all the Influenceurs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Influenceurs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InfluenceurDTO>> getAllInfluenceurs(
        InfluenceurCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Influenceurs by criteria: {}", criteria);

        Page<InfluenceurDTO> page = influenceurQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /influenceurs/count} : count all the influenceurs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInfluenceurs(InfluenceurCriteria criteria) {
        LOG.debug("REST request to count Influenceurs by criteria: {}", criteria);
        return ResponseEntity.ok().body(influenceurQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /influenceurs/:id} : get the "id" influenceur.
     *
     * @param id the id of the influenceurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the influenceurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InfluenceurDTO> getInfluenceur(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Influenceur : {}", id);
        Optional<InfluenceurDTO> influenceurDTO = influenceurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(influenceurDTO);
    }

    /**
     * {@code DELETE  /influenceurs/:id} : delete the "id" influenceur.
     *
     * @param id the id of the influenceurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInfluenceur(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Influenceur : {}", id);
        influenceurService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
