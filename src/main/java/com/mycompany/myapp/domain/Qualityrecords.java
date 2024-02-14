package com.mycompany.myapp.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Qualityrecords.
 */
@Table("qualityrecords")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Qualityrecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("supplier")
    private String supplier;

    @Column("test_2")
    private Integer test2;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Qualityrecords id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public Qualityrecords supplier(String supplier) {
        this.setSupplier(supplier);
        return this;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Integer getTest2() {
        return this.test2;
    }

    public Qualityrecords test2(Integer test2) {
        this.setTest2(test2);
        return this;
    }

    public void setTest2(Integer test2) {
        this.test2 = test2;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Qualityrecords)) {
            return false;
        }
        return getId() != null && getId().equals(((Qualityrecords) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Qualityrecords{" +
            "id=" + getId() +
            ", supplier='" + getSupplier() + "'" +
            ", test2=" + getTest2() +
            "}";
    }
}
