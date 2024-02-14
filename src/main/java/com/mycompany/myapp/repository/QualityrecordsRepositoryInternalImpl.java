package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Qualityrecords;
import com.mycompany.myapp.repository.rowmapper.QualityrecordsRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Qualityrecords entity.
 */
@SuppressWarnings("unused")
class QualityrecordsRepositoryInternalImpl extends SimpleR2dbcRepository<Qualityrecords, Long> implements QualityrecordsRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final QualityrecordsRowMapper qualityrecordsMapper;

    private static final Table entityTable = Table.aliased("qualityrecords", EntityManager.ENTITY_ALIAS);

    public QualityrecordsRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        QualityrecordsRowMapper qualityrecordsMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Qualityrecords.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.qualityrecordsMapper = qualityrecordsMapper;
    }

    @Override
    public Flux<Qualityrecords> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Qualityrecords> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = QualityrecordsSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Qualityrecords.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Qualityrecords> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Qualityrecords> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Qualityrecords process(Row row, RowMetadata metadata) {
        Qualityrecords entity = qualityrecordsMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Qualityrecords> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
