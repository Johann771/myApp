package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Qualityrecords;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Qualityrecords entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QualityrecordsRepository extends ReactiveCrudRepository<Qualityrecords, Long>, QualityrecordsRepositoryInternal {
    Flux<Qualityrecords> findAllBy(Pageable pageable);

    @Override
    <S extends Qualityrecords> Mono<S> save(S entity);

    @Override
    Flux<Qualityrecords> findAll();

    @Override
    Mono<Qualityrecords> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface QualityrecordsRepositoryInternal {
    <S extends Qualityrecords> Mono<S> save(S entity);

    Flux<Qualityrecords> findAllBy(Pageable pageable);

    Flux<Qualityrecords> findAll();

    Mono<Qualityrecords> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Qualityrecords> findAllBy(Pageable pageable, Criteria criteria);
}
