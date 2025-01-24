package com.brinvex.java;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;
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

    /// Also known as "Roulette-wheel selection" or "Fitness proportionate selection"
    /// - https://en.wikipedia.org/wiki/Fitness_proportionate_selection
    ///
    /// There is no need to sort items.
    /// - https://stackoverflow.com/questions/10531565/how-should-roulette-wheel-selection-be-organized-for-non-sorted-population-in-g
    /// - https://stackoverflow.com/questions/734668/roulette-wheel-selection-in-genetic-algorithm-population-needs-to-be-sorted-fir
    @SuppressWarnings("JavadocLinkAsPlainText")
    public static <T> T randomPick(
            Collection<Entry<T, Integer>> weightedItems,
            Supplier<Double> uniformRandGenerator
    ) {
        int n = weightedItems.size();
        if (n == 0) {
            return null;
        } else if (n == 1) {
            return weightedItems.iterator().next().getKey();
        }
        long weightSum = weightedItems.stream()
                .map(Entry::getValue)
                .mapToLong(Integer::longValue)
                .sum();
        if (weightSum <= 0) {
            throw new IllegalArgumentException("Weight sum must be greater than zero: weightSum=%s, weightedItems=%s"
                    .formatted(weightSum, weightedItems));
        }

        double rand = uniformRandGenerator.get() * weightSum;

        long weightCumul = 0L;
        for (Entry<T, Integer> e : weightedItems) {
            weightCumul = weightCumul + e.getValue();
            if (rand < weightCumul) {
                return e.getKey();
            }
        }
        throw new AssertionError("Unexpected fail - randomPick - rand=%s, weightSum=%s, weightedItems=%s"
                .formatted(rand, weightSum, weightedItems));
    }

}
