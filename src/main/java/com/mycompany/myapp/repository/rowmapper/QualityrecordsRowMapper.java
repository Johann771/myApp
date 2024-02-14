package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Qualityrecords;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Qualityrecords}, with proper type conversions.
 */
@Service
public class QualityrecordsRowMapper implements BiFunction<Row, String, Qualityrecords> {

    private final ColumnConverter converter;

    public QualityrecordsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Qualityrecords} stored in the database.
     */
    @Override
    public Qualityrecords apply(Row row, String prefix) {
        Qualityrecords entity = new Qualityrecords();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSupplier(converter.fromRow(row, prefix + "_supplier", String.class));
        entity.setTest2(converter.fromRow(row, prefix + "_test_2", Integer.class));
        return entity;
    }
}
