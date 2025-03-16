package test.com.brinvex.java;

import org.junit.jupiter.api.Test;

import static com.brinvex.java.collection.CollectionUtil.toChunks;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Arrays;

class CollectionChunkUtilTest {

    @Test
    void testToChunksNormalCase() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int chunkSize = 3;

        List<List<Integer>> chunks = toChunks(source, chunkSize);

        assertEquals(4, chunks.size());
        assertEquals(Arrays.asList(1, 2, 3), chunks.get(0));
        assertEquals(Arrays.asList(4, 5, 6), chunks.get(1));
        assertEquals(Arrays.asList(7, 8, 9), chunks.get(2));
        assertEquals(List.of(10), chunks.get(3));
    }

    @Test
    void testToChunksSingleElement() {
        List<Integer> source = List.of(1);
        int chunkSize = 1;

        List<List<Integer>> chunks = toChunks(source, chunkSize);

        assertEquals(1, chunks.size());
        assertEquals(List.of(1), chunks.get(0));
    }

    @Test
    void testToChunksEmptyList() {
        List<Integer> source = List.of();
        int chunkSize = 3;

        List<List<Integer>> chunks = toChunks(source, chunkSize);

        assertEquals(0, chunks.size());
    }

    @Test
    void testToChunksChunkSizeGreaterThanListSize() {
        List<Integer> source = Arrays.asList(1, 2);
        int chunkSize = 5;

        List<List<Integer>> chunks = toChunks(source, chunkSize);

        assertEquals(1, chunks.size());
        assertEquals(Arrays.asList(1, 2), chunks.get(0));
    }

    @Test
    void testToChunksChunkSizeOne() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        int chunkSize = 1;

        List<List<Integer>> chunks = toChunks(source, chunkSize);

        assertEquals(5, chunks.size());
        assertEquals(List.of(1), chunks.get(0));
        assertEquals(List.of(2), chunks.get(1));
        assertEquals(List.of(3), chunks.get(2));
        assertEquals(List.of(4), chunks.get(3));
        assertEquals(List.of(5), chunks.get(4));
    }

    @Test
    void testToChunksInvalidChunkSize() {
        List<Integer> source = Arrays.asList(1, 2, 3);

        assertThrows(IllegalArgumentException.class, () -> {
            toChunks(source, 0); // Invalid chunk size
        });

        assertThrows(IllegalArgumentException.class, () -> {
            toChunks(source, -1); // Invalid chunk size
        });
    }

    @Test
    void testToChunksNullList() {
        assertThrows(NullPointerException.class, () -> {
            toChunks(null, 3); // Null list
        });
    }
}
