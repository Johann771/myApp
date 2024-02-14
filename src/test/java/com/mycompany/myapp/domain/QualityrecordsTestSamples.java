package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QualityrecordsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Qualityrecords getQualityrecordsSample1() {
        return new Qualityrecords().id(1L).supplier("supplier1").test2(1);
    }

    public static Qualityrecords getQualityrecordsSample2() {
        return new Qualityrecords().id(2L).supplier("supplier2").test2(2);
    }

    public static Qualityrecords getQualityrecordsRandomSampleGenerator() {
        return new Qualityrecords()
            .id(longCount.incrementAndGet())
            .supplier(UUID.randomUUID().toString())
            .test2(intCount.incrementAndGet());
    }
}
