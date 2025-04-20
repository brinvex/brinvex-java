package test.com.brinvex.java;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static com.brinvex.java.collection.SortedListUtil.headList;
import static com.brinvex.java.collection.SortedListUtil.tailList;
import static org.junit.jupiter.api.Assertions.*;

class SortedListUtilTest {

    record Item(String name, LocalDate date) {
    }

    private static final Function<Item, LocalDate> DATE_EXTRACTOR = Item::date;

    @Test
    void testHeadListEmptyList() {
        List<Item> items = List.of();
        // Regardless of inclusive flag, an empty list always yields empty
        assertTrue(headList(items, DATE_EXTRACTOR, LocalDate.now(), true).isEmpty());
        assertTrue(headList(items, DATE_EXTRACTOR, LocalDate.now(), false).isEmpty());
    }

    @Test
    void testHeadListKeyBeforeFirst() {
        List<Item> items = List.of(
                new Item("a", LocalDate.parse("2020-01-10")),
                new Item("b", LocalDate.parse("2020-02-10"))
        );
        LocalDate key = LocalDate.parse("2020-01-01");

        // inclusive or exclusive on a key before the first element → always empty
        assertTrue(headList(items, DATE_EXTRACTOR, key, true).isEmpty());
        assertTrue(headList(items, DATE_EXTRACTOR, key, false).isEmpty());
    }

    @Test
    void testHeadListKeyAfterLast() {
        List<Item> items = List.of(
                new Item("a", LocalDate.parse("2020-01-10")),
                new Item("b", LocalDate.parse("2020-02-10"))
        );
        LocalDate key = LocalDate.parse("2020-03-01");

        // inclusive or exclusive on a key after the last → returns full list
        List<Item> headIncl = headList(items, DATE_EXTRACTOR, key, true);
        List<Item> headExcl = headList(items, DATE_EXTRACTOR, key, false);

        assertEquals(2, headIncl.size());
        assertEquals(List.of("a", "b"), headIncl.stream().map(Item::name).toList());

        assertEquals(2, headExcl.size());
        assertEquals(List.of("a", "b"), headExcl.stream().map(Item::name).toList());
    }

    @Test
    void testHeadListKeyInMiddle() {
        List<Item> items = List.of(
                new Item("a", LocalDate.parse("2020-01-01")),
                new Item("b", LocalDate.parse("2020-02-01")),
                new Item("c", LocalDate.parse("2020-03-01"))
        );
        LocalDate key = LocalDate.parse("2020-02-01");

        // Inclusive: include "b"
        List<Item> headIncl = headList(items, DATE_EXTRACTOR, key, true);
        assertEquals(2, headIncl.size());
        assertEquals(List.of("a", "b"), headIncl.stream().map(Item::name).toList());

        // Exclusive: exclude "b"
        List<Item> headExcl = headList(items, DATE_EXTRACTOR, key, false);
        assertEquals(1, headExcl.size());
        assertEquals(List.of("a"), headExcl.stream().map(Item::name).toList());
    }

    @Test
    void testHeadListKeyAtStart() {
        List<Item> items = List.of(
                new Item("a", LocalDate.parse("2020-01-01")),
                new Item("b", LocalDate.parse("2020-02-01"))
        );
        LocalDate key = LocalDate.parse("2020-01-01");

        // Inclusive: include "a"
        List<Item> headIncl = headList(items, DATE_EXTRACTOR, key, true);
        assertEquals(1, headIncl.size());
        assertEquals("a", headIncl.get(0).name());

        // Exclusive: exclude "a"
        List<Item> headExcl = headList(items, DATE_EXTRACTOR, key, false);
        assertTrue(headExcl.isEmpty());
    }

    @Test
    void testHeadListDuplicates() {
        List<Item> items = List.of(
                new Item("a", LocalDate.parse("2020-01-01")),
                new Item("b", LocalDate.parse("2020-01-01")),
                new Item("c", LocalDate.parse("2020-02-01"))
        );
        LocalDate key = LocalDate.parse("2020-01-01");

        // Inclusive: include both "a" and "b"
        List<Item> headIncl = headList(items, DATE_EXTRACTOR, key, true);
        assertEquals(2, headIncl.size());
        assertEquals(List.of("a", "b"), headIncl.stream().map(Item::name).toList());

        // Exclusive: exclude both "a" and "b"
        List<Item> headExcl = headList(items, DATE_EXTRACTOR, key, false);
        assertTrue(headExcl.isEmpty());
    }

    @Test
    void testHeadListDuplicates2() {
        List<Item> items = List.of(
                new Item("a", LocalDate.parse("2020-01-01")),
                new Item("b", LocalDate.parse("2020-01-01")),
                new Item("b", LocalDate.parse("2020-01-01")),
                new Item("b", LocalDate.parse("2020-01-01")),
                new Item("c", LocalDate.parse("2020-02-01")),
                new Item("c", LocalDate.parse("2020-02-01"))
        );
        LocalDate key = LocalDate.parse("2020-01-01");

        // Inclusive: include both "a" and "b"
        List<Item> headIncl = headList(items, DATE_EXTRACTOR, key, true);
        assertEquals(4, headIncl.size());
        assertEquals(List.of("a", "b", "b", "b"), headIncl.stream().map(Item::name).toList());

        // Exclusive: exclude both "a" and "b"
        List<Item> headExcl = headList(items, DATE_EXTRACTOR, key, false);
        assertTrue(headExcl.isEmpty());
    }

    @Test
    void testTailListInclusiveWithDuplicates() {
        List<Item> sorted = Arrays.asList(
                new Item("a", LocalDate.of(2025, 1, 1)),
                new Item("b1", LocalDate.of(2025, 2, 1)),
                new Item("b2", LocalDate.of(2025, 2, 1)),
                new Item("c", LocalDate.of(2025, 3, 1))
        );
        // inclusive: first ≥ 2025‑02‑01 → index 1
        List<Item> expected = sorted.subList(1, sorted.size());
        assertEquals(expected, tailList(sorted, DATE_EXTRACTOR, LocalDate.of(2025, 2, 1), true));
    }

    @Test
    void testTailListExclusiveWithDuplicates() {
        List<Item> sorted = Arrays.asList(
                new Item("a", LocalDate.of(2025, 1, 1)),
                new Item("b1", LocalDate.of(2025, 2, 1)),
                new Item("b2", LocalDate.of(2025, 2, 1)),
                new Item("c", LocalDate.of(2025, 3, 1))
        );
        // exclusive: first > 2025‑02‑01 → index 3
        List<Item> expected = sorted.subList(3, sorted.size());
        assertEquals(expected, tailList(sorted, DATE_EXTRACTOR, LocalDate.of(2025, 2, 1), false));
    }

    @Test
    void testTailListInclusiveKeyNotPresent() {
        List<Item> sorted = Arrays.asList(
                new Item("a", LocalDate.of(2025, 1, 1)),
                new Item("b", LocalDate.of(2025, 2, 1)),
                new Item("d", LocalDate.of(2025, 4, 1))
        );
        // 2025‑03‑01 not present; insertion point = first > → index 2
        List<Item> expected = sorted.subList(2, sorted.size());
        assertEquals(expected, tailList(sorted, DATE_EXTRACTOR, LocalDate.of(2025, 3, 1), true));
    }

    @Test
    void testTailListExclusiveKeyNotPresent() {
        List<Item> sorted = Arrays.asList(
                new Item("a", LocalDate.of(2025, 1, 1)),
                new Item("b", LocalDate.of(2025, 2, 1)),
                new Item("d", LocalDate.of(2025, 4, 1))
        );
        // same insertion point logic for exclusive
        List<Item> expected = sorted.subList(2, sorted.size());
        assertEquals(expected, tailList(sorted, DATE_EXTRACTOR, LocalDate.of(2025, 3, 1), false));
    }

    @Test
    void testTailListKeyBeforeFirst() {
        List<Item> sorted = Arrays.asList(
                new Item("x", LocalDate.of(2025, 5, 1)),
                new Item("y", LocalDate.of(2025, 6, 1))
        );
        // fromKey = 2025‑01‑01; insertion point = 0
        assertEquals(sorted, tailList(sorted, DATE_EXTRACTOR, LocalDate.of(2025, 1, 1), true));
        assertEquals(sorted, tailList(sorted, DATE_EXTRACTOR, LocalDate.of(2025, 1, 1), false));
    }

    @Test
    void testTailListKeyAfterLast() {
        List<Item> sorted = Arrays.asList(
                new Item("x", LocalDate.of(2025, 5, 1)),
                new Item("y", LocalDate.of(2025, 6, 1))
        );
        // fromKey = 2026‑01‑01; insertion point = size = 2 → empty
        assertTrue(tailList(sorted, DATE_EXTRACTOR, LocalDate.of(2026, 1, 1), true).isEmpty());
        assertTrue(tailList(sorted, DATE_EXTRACTOR, LocalDate.of(2026, 1, 1), false).isEmpty());
    }

    @Test
    void testTailListEmptySource() {
        List<Item> empty = Collections.emptyList();
        assertTrue(tailList(empty, DATE_EXTRACTOR, LocalDate.now(), true).isEmpty());
        assertTrue(tailList(empty, DATE_EXTRACTOR, LocalDate.now(), false).isEmpty());
    }
}
