package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Qualityrecords;
import com.mycompany.myapp.repository.QualityrecordsRepository;
import com.mycompany.myapp.service.QualityrecordsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Qualityrecords}.
 */
@RestController
@RequestMapping("/api/qualityrecords")
public class QualityrecordsResource {

    private final Logger log = LoggerFactory.getLogger(QualityrecordsResource.class);

    private static final String ENTITY_NAME = "qualityrecords";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QualityrecordsService qualityrecordsService;

    private final QualityrecordsRepository qualityrecordsRepository;

    public QualityrecordsResource(QualityrecordsService qualityrecordsService, QualityrecordsRepository qualityrecordsRepository) {
        this.qualityrecordsService = qualityrecordsService;
        this.qualityrecordsRepository = qualityrecordsRepository;
    }

    /**
     * {@code POST  /qualityrecords} : Create a new qualityrecords.
     *
     * @param qualityrecords the qualityrecords to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new qualityrecords, or with status {@code 400 (Bad Request)} if the qualityrecords has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Qualityrecords>> createQualityrecords(@Valid @RequestBody Qualityrecords qualityrecords)
        throws URISyntaxException {
        log.debug("REST request to save Qualityrecords : {}", qualityrecords);
        if (qualityrecords.getId() != null) {
            throw new BadRequestAlertException("A new qualityrecords cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return qualityrecordsService
            .save(qualityrecords)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/qualityrecords/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /qualityrecords/:id} : Updates an existing qualityrecords.
     *
     * @param id the id of the qualityrecords to save.
     * @param qualityrecords the qualityrecords to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qualityrecords,
     * or with status {@code 400 (Bad Request)} if the qualityrecords is not valid,
     * or with status {@code 500 (Internal Server Error)} if the qualityrecords couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Qualityrecords>> updateQualityrecords(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Qualityrecords qualityrecords
    ) throws URISyntaxException {
        log.debug("REST request to update Qualityrecords : {}, {}", id, qualityrecords);
        if (qualityrecords.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qualityrecords.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return qualityrecordsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return qualityrecordsService
                    .update(qualityrecords)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /qualityrecords/:id} : Partial updates given fields of an existing qualityrecords, field will ignore if it is null
     *
     * @param id the id of the qualityrecords to save.
     * @param qualityrecords the qualityrecords to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qualityrecords,
     * or with status {@code 400 (Bad Request)} if the qualityrecords is not valid,
     * or with status {@code 404 (Not Found)} if the qualityrecords is not found,
     * or with status {@code 500 (Internal Server Error)} if the qualityrecords couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Qualityrecords>> partialUpdateQualityrecords(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Qualityrecords qualityrecords
    ) throws URISyntaxException {
        log.debug("REST request to partial update Qualityrecords partially : {}, {}", id, qualityrecords);
        if (qualityrecords.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qualityrecords.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return qualityrecordsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Qualityrecords> result = qualityrecordsService.partialUpdate(qualityrecords);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /qualityrecords} : get all the qualityrecords.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qualityrecords in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Qualityrecords>>> getAllQualityrecords(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Qualityrecords");
        return qualityrecordsService
            .countAll()
            .zipWith(qualityrecordsService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /qualityrecords/:id} : get the "id" qualityrecords.
     *
     * @param id the id of the qualityrecords to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the qualityrecords, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Qualityrecords>> getQualityrecords(@PathVariable("id") Long id) {
        log.debug("REST request to get Qualityrecords : {}", id);
        Mono<Qualityrecords> qualityrecords = qualityrecordsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(qualityrecords);
    }

    /**
     * {@code DELETE  /qualityrecords/:id} : delete the "id" qualityrecords.
     *
     * @param id the id of the qualityrecords to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteQualityrecords(@PathVariable("id") Long id) {
        log.debug("REST request to delete Qualityrecords : {}", id);
        return qualityrecordsService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
