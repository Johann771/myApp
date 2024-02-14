package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.QualityrecordsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QualityrecordsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Qualityrecords.class);
        Qualityrecords qualityrecords1 = getQualityrecordsSample1();
        Qualityrecords qualityrecords2 = new Qualityrecords();
        assertThat(qualityrecords1).isNotEqualTo(qualityrecords2);

        qualityrecords2.setId(qualityrecords1.getId());
        assertThat(qualityrecords1).isEqualTo(qualityrecords2);

        qualityrecords2 = getQualityrecordsSample2();
        assertThat(qualityrecords1).isNotEqualTo(qualityrecords2);
    }
}
