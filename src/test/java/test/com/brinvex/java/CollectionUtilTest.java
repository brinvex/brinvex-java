package test.com.brinvex.java;

import com.brinvex.java.collection.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.brinvex.java.collection.CollectionUtil.rangeSafeSubMap;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollectionUtilTest {

    @Test
    public void removeAdjacentDuplicatesFromTreeMap() {
        TreeMap<Integer, Integer> m1 = new TreeMap<>(Map.of(1, 11, 2, 22, 3, 33, 33, 33, 44, 44));
        boolean modified = CollectionUtil.removeAdjacentValueDuplicates(m1);
        assertTrue(modified);

        TreeMap<Integer, Integer> m2 = new TreeMap<>(Map.of(1, 11, 2, 22, 3, 33, 44, 44));

        assertEquals(m2, m1);
    }

    @Test
    public void removeAdjacentDuplicatesFromEmptyCollection() {
        CollectionUtil.removeAdjacentDuplicates(emptyList());
        CollectionUtil.removeAdjacentDuplicates(emptyMap().entrySet());
        CollectionUtil.removeAdjacentDuplicates(emptySet());
    }

    @Test
    public void removeAdjacentDuplicatesFromSingletonCollection() {
        {
            List<Integer> c1 = List.of(1);
            CollectionUtil.removeAdjacentDuplicates(c1);
            assertEquals(List.of(1), c1);
        }
        {
            Set<Integer> c1 = Set.of(1);
            CollectionUtil.removeAdjacentDuplicates(c1);
            assertEquals(Set.of(1), c1);
        }
        {
            Map<Integer, Integer> c1 = Map.of(1, 2);
            CollectionUtil.removeAdjacentDuplicates(c1.entrySet());
            assertEquals(Map.of(1, 2), c1);
        }
    }

    @Test
    public void removeAdjacentDuplicatesFromList() {
        List<Integer> m1 = new ArrayList<>(List.of(1, 11, 2, 22, 3, 33, 33, 33, 44, 44));
        boolean modified = CollectionUtil.removeAdjacentDuplicates(m1);
        assertTrue(modified);

        List<Integer> m2 = new ArrayList<>(List.of(1, 11, 2, 22, 3, 33, 44));

        assertEquals(m2, m1);
    }

    @Test
    void sortedSubMap() {
        SortedMap<String, String> m1 = new TreeMap<>(Map.of("a", "A", "b", "B"));
        SortedMap<String, String> m2 = m1.subMap("b", "c");

        assertEquals(Map.of("b", "B"), m2);
        assertThrows(IllegalArgumentException.class, () -> m2.subMap("a", "a"));
        assertEquals(emptyMap(), rangeSafeSubMap(m2, "a", "a"));
        assertEquals(emptyMap(), rangeSafeSubMap(m2, "A", "A"));
        assertEquals(emptyMap(), rangeSafeSubMap(m1, "A", "a"));
        assertEquals(Map.of("a", "A"), rangeSafeSubMap(m1, "A", "b"));
        assertEquals(m1, rangeSafeSubMap(m1, "A", "c"));
        assertEquals(emptyMap(), rangeSafeSubMap(m1, "a", "a"));
        assertEquals(emptyMap(), rangeSafeSubMap(m1, "b", "b"));

        m1.put("b2", "B2");
        assertEquals(Map.of("b", "B", "b2", "B2"), m2);

        m1.put("k", "K");
        assertEquals(Map.of("b", "B", "b2", "B2"), m2);

        assertThrows(IllegalArgumentException.class, () -> m1.subMap("b", "b").put("b3", "B3"));
        assertThrows(IllegalArgumentException.class, () -> m1.subMap("b", "b").put("b", "B3"));

    }

    @Test
    void navigableSubMap() {
        NavigableMap<String, String> m1 = new TreeMap<>(Map.of("a", "A", "b", "B"));
        NavigableMap<String, String> m2 = m1.subMap("b", true, "c", false);

        assertEquals(Map.of("b", "B"), m2);
        assertThrows(IllegalArgumentException.class, () -> m2.subMap("a", "a"));
        assertEquals(emptyMap(), rangeSafeSubMap(m2, "a", "a"));
        assertEquals(emptyMap(), rangeSafeSubMap(m2, "A", "A"));
        assertEquals(emptyMap(), rangeSafeSubMap(m1, "A", "a"));
        assertEquals(Map.of("a", "A"), rangeSafeSubMap(m1, "A", "b"));
        assertEquals(m1, rangeSafeSubMap(m1, "A", "c"));
        assertEquals(emptyMap(), rangeSafeSubMap(m1, "a", "a"));
        assertEquals(emptyMap(), rangeSafeSubMap(m1, "b", "b"));

        m1.put("b2", "B2");
        assertEquals(Map.of("b", "B", "b2", "B2"), m2);

        m1.put("k", "K");
        assertEquals(Map.of("b", "B", "b2", "B2"), m2);

        assertThrows(IllegalArgumentException.class, () -> m1.subMap("b", "b").put("b3", "B3"));
        assertThrows(IllegalArgumentException.class, () -> m1.subMap("b", "b").put("b", "B3"));

    }
}
