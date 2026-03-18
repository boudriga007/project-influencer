package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ScoreSentimentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static ScoreSentiment getScoreSentimentSample1() {
        return new ScoreSentiment().id(1L);
    }

    public static ScoreSentiment getScoreSentimentSample2() {
        return new ScoreSentiment().id(2L);
    }

    public static ScoreSentiment getScoreSentimentRandomSampleGenerator() {
        return new ScoreSentiment().id(longCount.incrementAndGet());
    }
}
