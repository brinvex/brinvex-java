package test.com.brinvex.java;

import org.junit.jupiter.api.Test;

import static com.brinvex.java.collection.CollectionUtil.toChunks;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class CollectionChunkUtilTest {

    @Test
    void toChunksNormalCase() {
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
    void toChunksSingleElement() {
        List<Integer> source = List.of(1);
        int chunkSize = 1;

        List<List<Integer>> chunks = toChunks(source, chunkSize);

        assertEquals(1, chunks.size());
        assertEquals(List.of(1), chunks.get(0));
    }

    @Test
    void toChunksEmptyList() {
        List<Integer> source = List.of();
        int chunkSize = 3;

        List<List<Integer>> chunks = toChunks(source, chunkSize);

        assertEquals(0, chunks.size());
    }

    @Test
    void toChunksChunkSizeGreaterThanListSize() {
        List<Integer> source = Arrays.asList(1, 2);
        int chunkSize = 5;

        List<List<Integer>> chunks = toChunks(source, chunkSize);

        assertEquals(1, chunks.size());
        assertEquals(Arrays.asList(1, 2), chunks.get(0));
    }

    @Test
    void toChunksChunkSizeOne() {
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
    void toChunksInvalidChunkSize() {
        List<Integer> source = Arrays.asList(1, 2, 3);

        assertThrows(IllegalArgumentException.class, () -> {
            toChunks(source, 0); // Invalid chunk size
        });

        assertThrows(IllegalArgumentException.class, () -> {
            toChunks(source, -1); // Invalid chunk size
        });
    }

    @Test
    void toChunksNullList() {
        assertThrows(NullPointerException.class, () -> {
            toChunks((List<Object>) null, 3); // Null list
        });
    }

    // ==============================
    // Tests for toChunks(Iterable<T>, int)
    // ==============================

    @Test
    void emptyIterable() {
        Iterable<Integer> emptyIterable = Collections.emptyList();
        List<List<Integer>> result = toChunks(emptyIterable, 3);
        assertTrue(result.isEmpty(), "Expected no chunks for an empty iterable");
    }

    @Test
    void singleChunkIterable() {
        List<String> input = Arrays.asList("a", "b");
        List<List<String>> result = toChunks(input, 5);
        assertEquals(1, result.size(), "Expected a single chunk when chunk size exceeds input size");
        assertEquals(Arrays.asList("a", "b"), result.get(0), "Chunk content did not match the expected values");
    }

    @Test
    void multipleChunksExactDivisionIterable() {
        List<Integer> input = Arrays.asList(1, 2, 3, 4);
        List<List<Integer>> result = toChunks(input, 2);
        assertEquals(2, result.size(), "Expected two chunks for exact division");
        assertEquals(Arrays.asList(1, 2), result.get(0));
        assertEquals(Arrays.asList(3, 4), result.get(1));
    }

    @Test
    void multipleChunksWithRemainderIterable() {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5);
        List<List<Integer>> result = toChunks(input, 2);
        assertEquals(3, result.size(), "Expected three chunks when the last chunk is smaller");
        assertEquals(Arrays.asList(1, 2), result.get(0));
        assertEquals(Arrays.asList(3, 4), result.get(1));
        assertEquals(Collections.singletonList(5), result.get(2));
    }

    @Test
    void nullIterableSource() {
        assertThrows(NullPointerException.class, () ->
                        toChunks((Iterable<Object>) null, 3),
                "Expected NullPointerException for null iterable source"
        );
    }

    @Test
    void illegalChunkSizeIterable() {
        List<String> input = Arrays.asList("a", "b", "c");
        assertThrows(IllegalArgumentException.class, () ->
                        toChunks(input, 0),
                "Expected IllegalArgumentException for non-positive chunk size"
        );
    }

    // ==============================
    // Tests for toChunks(Stream<T>, int)
    // ==============================

    @Test
    void emptyStream() {
        Stream<Integer> emptyStream = Stream.empty();
        List<List<Integer>> result = toChunks(emptyStream, 3);
        assertTrue(result.isEmpty(), "Expected no chunks for an empty stream");
    }

    @Test
    void singleChunkStream() {
        Stream<String> inputStream = Stream.of("a", "b");
        List<List<String>> result = toChunks(inputStream, 5);
        assertEquals(1, result.size(), "Expected a single chunk when chunk size exceeds stream size");
        assertEquals(Arrays.asList("a", "b"), result.get(0), "Chunk content did not match the expected values");
    }

    @Test
    void multipleChunksExactDivisionStream() {
        Stream<Integer> inputStream = IntStream.rangeClosed(1, 4).boxed();
        List<List<Integer>> result = toChunks(inputStream, 2);
        assertEquals(2, result.size(), "Expected two chunks for exact division");
        assertEquals(Arrays.asList(1, 2), result.get(0));
        assertEquals(Arrays.asList(3, 4), result.get(1));
    }

    @Test
    void multipleChunksWithRemainderStream() {
        Stream<Integer> inputStream = IntStream.rangeClosed(1, 5).boxed();
        List<List<Integer>> result = toChunks(inputStream, 2);
        assertEquals(3, result.size(), "Expected three chunks when the last chunk is smaller");
        assertEquals(Arrays.asList(1, 2), result.get(0));
        assertEquals(Arrays.asList(3, 4), result.get(1));
        assertEquals(Collections.singletonList(5), result.get(2));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void nullStreamSource() {
        assertThrows(NullPointerException.class, () ->
                        toChunks((Stream<Object>) null, 3),
                "Expected NullPointerException for null stream source"
        );
    }

    @Test
    void illegalChunkSizeStream() {
        Stream<String> inputStream = Stream.of("a", "b", "c");
        assertThrows(IllegalArgumentException.class, () ->
                        toChunks(inputStream, 0),
                "Expected IllegalArgumentException for non-positive chunk size"
        );
    }

}
