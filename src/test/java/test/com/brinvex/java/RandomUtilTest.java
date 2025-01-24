package test.com.brinvex.java;

import com.brinvex.java.RandomUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.brinvex.java.RandomUtil.randomPick;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomUtilTest {

    @Test
    public void randomDoublesSummingToOne() {
        Random random = new Random();
        {
            double[] randDoubles = RandomUtil.randomDoublesSummingToOne(1, random::nextDouble);
            assertEquals(1, randDoubles.length);
            assertEquals(1.0, randDoubles[0]);
        }
        {
            double[] randDoubles = RandomUtil.randomDoublesSummingToOne(2, random::nextDouble);
            assertEquals(2, randDoubles.length);
            assertTrue(randDoubles[0] >= 0);
            assertTrue(randDoubles[0] <= 1);
            assertTrue(randDoubles[1] >= 0);
            assertTrue(randDoubles[1] <= 1);
            assertEquals(1.0, randDoubles[0] + randDoubles[1]);
        }
        {
            double[] randDoubles = RandomUtil.randomDoublesSummingToOne(3, random::nextDouble);
            assertEquals(3, randDoubles.length);
            assertTrue(randDoubles[0] >= 0);
            assertTrue(randDoubles[0] <= 1);
            assertTrue(randDoubles[1] >= 0);
            assertTrue(randDoubles[1] <= 1);
            assertTrue(randDoubles[2] >= 0);
            assertTrue(randDoubles[2] <= 1);
            assertEquals(1.0, randDoubles[0] + randDoubles[1] + randDoubles[2]);
        }
        {
            double[] randDoubles = RandomUtil.randomDoublesSummingToOne(4, random::nextDouble);
            assertEquals(4, randDoubles.length);
            assertTrue(randDoubles[0] >= 0);
            assertTrue(randDoubles[0] <= 1);
            assertTrue(randDoubles[1] >= 0);
            assertTrue(randDoubles[1] <= 1);
            assertTrue(randDoubles[2] >= 0);
            assertTrue(randDoubles[2] <= 1);
            assertTrue(randDoubles[3] >= 0);
            assertTrue(randDoubles[3] <= 1);
            assertEquals(1.0, randDoubles[0] + randDoubles[1] + randDoubles[2] + randDoubles[3]);
        }
    }

    // Test for picking from a list with positive weights
    @Test
    public void testRandomPickPositiveWeights() {
        // Weighted items with values for "A", "B", and "C"
        Collection<Map.Entry<String, Integer>> weightedItems = List.of(
                Map.entry("A", 1),
                Map.entry("B", 2),
                Map.entry("C", 3)
        );

        // Use Math.random for generating random numbers
        String result = randomPick(weightedItems, Math::random);

        // Ensure that the result is one of the items, and it should be more likely to pick "C"
        assertTrue(weightedItems.stream().anyMatch(entry -> entry.getKey().equals(result)));
    }

    // Test for picking from a collection with a single item
    @Test
    public void testRandomPickSingleItem() {
        // Weighted item with only one element
        Collection<Map.Entry<String, Integer>> weightedItems = List.of(
                Map.entry("A", 5)
        );

        String result = randomPick(weightedItems, Math::random);

        // Ensure the single item is always selected
        assertEquals("A", result);
    }

    // Test for empty collection
    @Test
    public void testRandomPickEmptyCollection() {
        // Empty collection
        Collection<Map.Entry<String, Integer>> weightedItems = Collections.emptyList();

        String result = randomPick(weightedItems, Math::random);

        // Should return null for empty collection
        assertNull(result);
    }

    // Test for invalid (zero or negative) weights
    @Test
    public void testRandomPickInvalidWeights() {
        // Invalid weights (sum <= 0)
        Collection<Map.Entry<String, Integer>> weightedItems = List.of(
                Map.entry("A", -1),
                Map.entry("B", 0),
                Map.entry("C", 0)
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                randomPick(weightedItems, Math::random));

        // Assert the exception message contains relevant details
        assertTrue(exception.getMessage().contains("Weight sum must be greater than zero"));
    }

    // Test for a large number of items
    @Test
    public void testRandomPickLargeNumberOfItems() {
        // Create 1,000,000 items with weight increasing by 1
        List<Map.Entry<String, Integer>> weightedItems = new ArrayList<>();
        for (int i = 1; i <= 1_000_000; i++) {
            weightedItems.add(Map.entry("Item" + i, i));
        }

        // Pick an item from the collection
        String result = randomPick(weightedItems, Math::random);

        // Check if the result is one of the valid items
        assertTrue(weightedItems.stream().anyMatch(entry -> entry.getKey().equals(result)));
    }

    // Test for random distribution (should be biased towards higher weights)
    @Test
    public void testRandomPickDistribution() {
        // Weighted items with higher weight on "C"
        Collection<Map.Entry<String, Integer>> weightedItems = List.of(
                Map.entry("A", 4),
                Map.entry("B", 1),
                Map.entry("C", 5)
        );

        // Simulate multiple random picks
        Map<String, Integer> pickCounts = new HashMap<>();
        for (int i = 0; i < 1_000_000; i++) {
            String result = randomPick(weightedItems, Math::random);
            pickCounts.put(result, pickCounts.getOrDefault(result, 0) + 1);
        }

        // C should be picked much more often than A or B
        assertTrue(pickCounts.get("C") > pickCounts.get("A"));
        assertTrue(pickCounts.get("A") > pickCounts.get("B"));
    }
}

