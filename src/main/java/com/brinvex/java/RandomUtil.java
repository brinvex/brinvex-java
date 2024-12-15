package com.brinvex.java;

import java.util.Arrays;
import java.util.function.Supplier;

public class RandomUtil {

    public static double[] randomDoublesSummingToOne(
            int n,
            Supplier<Double> uniformRandGenerator
    ) {
        if (n == 1) {
            return new double[]{1.0};
        }
        double[] cumulative = new double[n - 1];
        for (int i = 0; i < n - 1; i++) {
            cumulative[i] = uniformRandGenerator.get();
        }
        Arrays.sort(cumulative);
        double[] results = new double[n];
        results[0] = cumulative[0];
        for (int i = 1; i < n - 1; i++) {
            results[i] = cumulative[i] - cumulative[i - 1];
        }
        results[n - 1] = 1.0 - cumulative[n - 2];
        return results;
    }

}
