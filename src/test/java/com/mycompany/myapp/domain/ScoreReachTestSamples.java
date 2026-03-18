package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ScoreReachTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static ScoreReach getScoreReachSample1() {
        return new ScoreReach().id(1L).nbFollowers(1L);
    }

    public static ScoreReach getScoreReachSample2() {
        return new ScoreReach().id(2L).nbFollowers(2L);
    }

    public static ScoreReach getScoreReachRandomSampleGenerator() {
        return new ScoreReach().id(longCount.incrementAndGet()).nbFollowers(longCount.incrementAndGet());
    }
}
