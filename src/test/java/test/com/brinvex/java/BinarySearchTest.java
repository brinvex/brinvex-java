package test.com.brinvex.java;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.brinvex.java.collection.SortedListUtil.binarySearch;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BinarySearchTest {

    // Record to represent items with a name and a date
    record Item(String name, LocalDate date) {
    }

    // Comparator for sorting by 'date' attribute of Item
    private static final Comparator<Item> DATE_COMPARATOR = Comparator.comparing(Item::date);

    // Test case for an exact match
    @Test
    void testExactMatch() {
        // Test data with sorted dates
        List<Item> items = List.of(
                new Item("a", LocalDate.parse("2020-01-01")),
                new Item("b", LocalDate.parse("2020-02-01")),
                new Item("c", LocalDate.parse("2020-03-01"))
        );

        // The key we're searching for
        LocalDate key = LocalDate.parse("2020-02-01");

        // Custom binary search using the key
        int custom = binarySearch(items, key, Item::date);

        // Standard binary search using Collections
        int standard = Collections.binarySearch(items, new Item("", key), DATE_COMPARATOR);

        // Assert that both searches give the same result
        assertEquals(1, custom);  // We expect the index to be 1
        assertEquals(custom, standard);  // Verify both methods return the same index
    }

    // Test case for a not-found value, which should return an insertion point in the middle
    @Test
    void testNotFoundInsertMiddle() {
        List<Item> items = List.of(
                new Item("a", LocalDate.parse("2020-01-01")),
                new Item("b", LocalDate.parse("2020-02-01")),
                new Item("c", LocalDate.parse("2020-03-01"))
        );

        LocalDate key = LocalDate.parse("2020-01-15");

        int custom = binarySearch(items, key, Item::date);
        int standard = Collections.binarySearch(items, new Item("", key), DATE_COMPARATOR);

        // Expected insertion point is -2 (insert between "a" and "b")
        assertEquals(-2, custom);
        assertEquals(custom, standard);
    }

    // Test case for a not-found value at the start of the list
    @Test
    void testNotFoundInsertStart() {
        List<Item> items = List.of(
                new Item("a", LocalDate.parse("2020-01-10")),
                new Item("b", LocalDate.parse("2020-02-01"))
        );

        LocalDate key = LocalDate.parse("2020-01-01");

        int custom = binarySearch(items, key, Item::date);
        int standard = Collections.binarySearch(items, new Item("", key), DATE_COMPARATOR);

        // Expected insertion point is -1 (insert before the first element)
        assertEquals(-1, custom);
        assertEquals(custom, standard);
    }

    // Test case for a not-found value at the end of the list
    @Test
    void testNotFoundInsertEnd() {
        List<Item> items = List.of(
                new Item("a", LocalDate.parse("2020-01-10")),
                new Item("b", LocalDate.parse("2020-02-01"))
        );

        LocalDate key = LocalDate.parse("2020-03-01");

        int custom = binarySearch(items, key, Item::date);
        int standard = Collections.binarySearch(items, new Item("", key), DATE_COMPARATOR);

        // Expected insertion point is -3 (insert after the last element)
        assertEquals(-3, custom);
        assertEquals(custom, standard);
    }

    // Test case for duplicates: searching for an item that exists multiple times
    @Test
    void testDuplicatesFindAnyOccurrence() {
        List<Item> items = List.of(
                new Item("a", LocalDate.parse("2020-01-01")),
                new Item("b", LocalDate.parse("2020-01-01")),
                new Item("c", LocalDate.parse("2020-02-01"))
        );

        LocalDate key = LocalDate.parse("2020-01-01");

        int custom = binarySearch(items, key, Item::date);
        int standard = Collections.binarySearch(items, new Item("", key), DATE_COMPARATOR);

        // Custom binary search can return any matching index (0 or 1 in this case)
        assertTrue(custom == 0 || custom == 1);
        assertEquals(key, items.get(custom).date());  // Validate the date matches
        assertEquals(custom, standard);  // Compare with standard binary search result
    }

    // Test case for an empty list, which should always return -1
    @Test
    void testEmptyList() {
        List<Item> items = List.of();
        LocalDate key = LocalDate.parse("2020-01-01");

        int custom = binarySearch(items, key, Item::date);
        int standard = Collections.binarySearch(items, new Item("", key), DATE_COMPARATOR);

        // For an empty list, both methods should return -1
        assertEquals(-1, custom);
        assertEquals(custom, standard);
    }
}
