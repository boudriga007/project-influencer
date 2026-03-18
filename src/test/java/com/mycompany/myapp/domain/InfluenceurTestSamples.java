package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InfluenceurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Influenceur getInfluenceurSample1() {
        return new Influenceur().id(1L).nom("nom1").username("username1").photoUrl("photoUrl1").bio("bio1");
    }

    public static Influenceur getInfluenceurSample2() {
        return new Influenceur().id(2L).nom("nom2").username("username2").photoUrl("photoUrl2").bio("bio2");
    }

    public static Influenceur getInfluenceurRandomSampleGenerator() {
        return new Influenceur()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .username(UUID.randomUUID().toString())
            .photoUrl(UUID.randomUUID().toString())
            .bio(UUID.randomUUID().toString());
    }
}
