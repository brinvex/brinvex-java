package com.brinvex.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ChunkUtil {

    /**
     * Splits a given list into smaller sublists (chunks) of a specified size.
     * <p>
     * The returned sublists are backed by the original list, meaning changes
     * in the sublists reflect in the source list and vice versa. To create
     * independent chunks, wrap the sublist in a new {@code ArrayList<>}.
     * </p>
     *
     * @param <T>       the type of elements in the list
     * @param source    the list to be chunked
     * @param chunkSize the maximum size of each chunk (must be greater than zero)
     * @return a list of sublists, each containing at most {@code chunkSize} elements
     * @throws IllegalArgumentException if {@code chunkSize} is less than or equal to zero
     * @throws NullPointerException if {@code source} is null
     */
    public static <T> List<List<T>> toChunks(List<T> source, int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("Chunk size must be greater than zero");
        }

        List<List<T>> chunks = new ArrayList<>();
        for (int i = 0; i < source.size(); i += chunkSize) {
            chunks.add(source.subList(i, Math.min(source.size(), i + chunkSize)));
        }
        return chunks;
    }

    /**
     * Splits an {@code Iterable} into smaller sublists (chunks) of a specified size.
     * <p>
     * Unlike {@code List.subList()}, the returned sublists are independent copies
     * and are not backed by the original iterable. Modifications to the sublists
     * will not affect the original source.
     * </p>
     *
     * @param <T>       the type of elements in the iterable
     * @param source    the iterable to be chunked
     * @param chunkSize the maximum size of each chunk (must be greater than zero)
     * @return a list of sublists, each containing at most {@code chunkSize} elements
     * @throws IllegalArgumentException if {@code chunkSize} is less than or equal to zero
     * @throws NullPointerException if {@code source} is null
     */
    public static <T> List<List<T>> toChunks(Iterable<T> source, int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("Chunk size must be greater than zero");
        }

        Iterator<T> iterator = source.iterator();
        List<List<T>> chunks = new ArrayList<>();

        while (iterator.hasNext()) {
            List<T> chunk = new ArrayList<>();
            for (int i = 0; i < chunkSize && iterator.hasNext(); i++) {
                chunk.add(iterator.next());
            }
            chunks.add(chunk);
        }

        return chunks;
    }

    /**
     * Splits a {@link Stream} into smaller sublists (chunks) of a specified size.
     * <p>
     * This method converts the provided stream into an {@code Iterable} using
     * {@code source::iterator} and delegates the chunking logic to
     * {@link #toChunks(Iterable, int)}. The resulting sublists are independent copies,
     * meaning they are not backed by the original source.
     * </p>
     *
     * @param <T>       the type of elements in the stream
     * @param source    the stream to be chunked
     * @param chunkSize the maximum size of each chunk (must be greater than zero)
     * @return a list of sublists, each containing at most {@code chunkSize} elements
     * @throws IllegalArgumentException if {@code chunkSize} is less than or equal to zero
     * @throws NullPointerException if {@code source} is null
     * @see #toChunks(Iterable, int)
     */
    public static <T> List<List<T>> toChunks(Stream<T> source, int chunkSize) {
        return toChunks(source::iterator, chunkSize);
    }

    public static <T> List<List<T>> toChunks500(List<T> source) {
        return toChunks(source, 500);
    }

    public static <T> List<List<T>> toChunks1000(List<T> source) {
        return toChunks(source, 1000);
    }

    public static <T> List<List<T>> toChunks500(Iterable<T> source) {
        return toChunks(source, 500);
    }

    public static <T> List<List<T>> toChunks1000(Iterable<T> source) {
        return toChunks(source, 1000);
    }

    public static <T> List<List<T>> toChunks500(Stream<T> source) {
        return toChunks(source, 500);
    }

    public static <T> List<List<T>> toChunks1000(Stream<T> source) {
        return toChunks(source, 1000);
    }


}
