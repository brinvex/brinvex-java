package test.com.brinvex.java;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.brinvex.java.collection.CollectionUtil.sumConsecutiveEntries;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SumConsecutiveEntriesTest {

    @Test
    void testEmptyInput() {
        SortedMap<LocalDate, BigDecimal> input = new TreeMap<>();
        var result = sumConsecutiveEntries(input, 3, i -> 0);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSingleEntry() {
        SortedMap<LocalDate, BigDecimal> input = new TreeMap<>();
        input.put(LocalDate.of(2025, 11, 1), BigDecimal.TEN);

        var result = sumConsecutiveEntries(input, 3, i -> 0);

        assertEquals(1, result.size());
        assertEquals(BigDecimal.TEN, result.get(LocalDate.of(2025, 11, 1)));
    }

    @Test
    void testTwoConsecutiveDays_GroupSize2_KeepFirst() {
        SortedMap<LocalDate, BigDecimal> input = new TreeMap<>();
        input.put(LocalDate.of(2025, 11, 1), BigDecimal.valueOf(10));
        input.put(LocalDate.of(2025, 11, 2), BigDecimal.valueOf(5));

        var result = sumConsecutiveEntries(input, 2, i -> 0); // always pick first

        assertEquals(1, result.size());
        assertEquals(BigDecimal.valueOf(15), result.get(LocalDate.of(2025, 11, 1)));
    }

    @Test
    void testTwoConsecutiveDays_GroupSize2_KeepSecond() {
        SortedMap<LocalDate, BigDecimal> input = new TreeMap<>();
        input.put(LocalDate.of(2025, 11, 1), BigDecimal.valueOf(10));
        input.put(LocalDate.of(2025, 11, 2), BigDecimal.valueOf(5));

        var result = sumConsecutiveEntries(input, 2, i -> 1); // always pick second

        assertEquals(1, result.size());
        assertEquals(BigDecimal.valueOf(15), result.get(LocalDate.of(2025, 11, 2)));
    }

    @Test
    void testThreeConsecutiveDays_GroupSize3_PickMiddle() {
        SortedMap<LocalDate, BigDecimal> input = new TreeMap<>();
        input.put(LocalDate.of(2025, 11, 1), BigDecimal.valueOf(1));
        input.put(LocalDate.of(2025, 11, 2), BigDecimal.valueOf(2));
        input.put(LocalDate.of(2025, 11, 3), BigDecimal.valueOf(3));

        var result = sumConsecutiveEntries(input, 3, i -> 1); // pick middle date

        assertEquals(1, result.size());
        assertEquals(BigDecimal.valueOf(6), result.get(LocalDate.of(2025, 11, 2)));
    }

    @Test
    void testMultipleGroups() {
        SortedMap<LocalDate, BigDecimal> input = new TreeMap<>();
        input.put(LocalDate.of(2025, 11, 1), BigDecimal.valueOf(10));
        input.put(LocalDate.of(2025, 11, 2), BigDecimal.valueOf(5));
        input.put(LocalDate.of(2025, 11, 4), BigDecimal.valueOf(3));
        input.put(LocalDate.of(2025, 11, 5), BigDecimal.valueOf(2));

        // Pick first in both groups
        var result = sumConsecutiveEntries(input, 2, i -> 0);

        assertEquals(2, result.size());
        assertEquals(BigDecimal.valueOf(15), result.get(LocalDate.of(2025, 11, 1)));
        assertEquals(BigDecimal.valueOf(5), result.get(LocalDate.of(2025, 11, 4)));
    }

    @Test
    void testMultipleGroups2() {
        SortedMap<LocalDate, BigDecimal> input = new TreeMap<>();
        input.put(LocalDate.of(2025, 11, 1), BigDecimal.valueOf(10));
        input.put(LocalDate.of(2025, 11, 2), BigDecimal.valueOf(5));
        input.put(LocalDate.of(2025, 11, 3), BigDecimal.valueOf(7));
        input.put(LocalDate.of(2025, 11, 4), BigDecimal.valueOf(3));
        input.put(LocalDate.of(2025, 11, 5), BigDecimal.valueOf(2));

        // Pick first in both groups
        var result = sumConsecutiveEntries(input, 2, i -> 0);

        assertEquals(3, result.size());
        assertEquals(BigDecimal.valueOf(15), result.get(LocalDate.of(2025, 11, 1)));
        assertEquals(BigDecimal.valueOf(10), result.get(LocalDate.of(2025, 11, 3)));
        assertEquals(BigDecimal.valueOf(2), result.get(LocalDate.of(2025, 11, 5)));
    }

    @Test
    void testMultipleGroups3() {
        SortedMap<LocalDate, BigDecimal> input = new TreeMap<>();
        input.put(LocalDate.of(2025, 11, 1), BigDecimal.valueOf(10));
        input.put(LocalDate.of(2025, 11, 2), BigDecimal.valueOf(5));
        input.put(LocalDate.of(2025, 11, 3), BigDecimal.valueOf(7));
        input.put(LocalDate.of(2025, 11, 4), BigDecimal.valueOf(3));
        input.put(LocalDate.of(2025, 11, 5), BigDecimal.valueOf(2));
        input.put(LocalDate.of(2025, 11, 8), BigDecimal.valueOf(20));
        input.put(LocalDate.of(2025, 11, 9), BigDecimal.valueOf(4));

        // Pick first in both groups
        var result = sumConsecutiveEntries(input, 2, i -> 0);

        assertEquals(4, result.size());
        assertEquals(BigDecimal.valueOf(15), result.get(LocalDate.of(2025, 11, 1)));
        assertEquals(BigDecimal.valueOf(10), result.get(LocalDate.of(2025, 11, 3)));
        assertEquals(BigDecimal.valueOf(2), result.get(LocalDate.of(2025, 11, 5)));
        assertEquals(BigDecimal.valueOf(24), result.get(LocalDate.of(2025, 11, 8)));
    }

    @Test
    void testNonConsecutiveDays() {
        SortedMap<LocalDate, BigDecimal> input = new TreeMap<>();
        input.put(LocalDate.of(2025, 11, 1), BigDecimal.valueOf(10));
        input.put(LocalDate.of(2025, 11, 3), BigDecimal.valueOf(5));

        var result = sumConsecutiveEntries(input, 2, i -> 0);

        assertEquals(2, result.size());
        assertEquals(BigDecimal.valueOf(10), result.get(LocalDate.of(2025, 11, 1)));
        assertEquals(BigDecimal.valueOf(5), result.get(LocalDate.of(2025, 11, 3)));
    }
}
