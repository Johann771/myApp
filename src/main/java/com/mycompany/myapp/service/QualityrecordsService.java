package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Qualityrecords;
import com.mycompany.myapp.repository.QualityrecordsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Qualityrecords}.
 */
@Service
@Transactional
public class QualityrecordsService {

    private final Logger log = LoggerFactory.getLogger(QualityrecordsService.class);

    private final QualityrecordsRepository qualityrecordsRepository;

    public QualityrecordsService(QualityrecordsRepository qualityrecordsRepository) {
        this.qualityrecordsRepository = qualityrecordsRepository;
    }

    /**
     * Save a qualityrecords.
     *
     * @param qualityrecords the entity to save.
     * @return the persisted entity.
     */
    public Mono<Qualityrecords> save(Qualityrecords qualityrecords) {
        log.debug("Request to save Qualityrecords : {}", qualityrecords);
        return qualityrecordsRepository.save(qualityrecords);
    }

    /**
     * Update a qualityrecords.
     *
     * @param qualityrecords the entity to save.
     * @return the persisted entity.
     */
    public Mono<Qualityrecords> update(Qualityrecords qualityrecords) {
        log.debug("Request to update Qualityrecords : {}", qualityrecords);
        return qualityrecordsRepository.save(qualityrecords);
    }

    /**
     * Partially update a qualityrecords.
     *
     * @param qualityrecords the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Qualityrecords> partialUpdate(Qualityrecords qualityrecords) {
        log.debug("Request to partially update Qualityrecords : {}", qualityrecords);

        return qualityrecordsRepository
            .findById(qualityrecords.getId())
            .map(existingQualityrecords -> {
                if (qualityrecords.getSupplier() != null) {
                    existingQualityrecords.setSupplier(qualityrecords.getSupplier());
                }
                if (qualityrecords.getTest2() != null) {
                    existingQualityrecords.setTest2(qualityrecords.getTest2());
                }

                return existingQualityrecords;
            })
            .flatMap(qualityrecordsRepository::save);
    }

    /**
     * Get all the qualityrecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Qualityrecords> findAll(Pageable pageable) {
        log.debug("Request to get all Qualityrecords");
        return qualityrecordsRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of qualityrecords available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return qualityrecordsRepository.count();
    }

    /**
     * Get one qualityrecords by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Qualityrecords> findOne(Long id) {
        log.debug("Request to get Qualityrecords : {}", id);
        return qualityrecordsRepository.findById(id);
    }

    /**
     * Delete the qualityrecords by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Qualityrecords : {}", id);
        return qualityrecordsRepository.deleteById(id);
    }
}
