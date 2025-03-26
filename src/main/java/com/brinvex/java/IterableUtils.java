package com.brinvex.java;

import java.util.Iterator;

public class IterableUtils {

    public static <T> void forEachPair(Iterable<T> iterable, PairConsumer<T> consumer) {
        if (iterable == null) return;

        Iterator<T> iterator = iterable.iterator();
        if (!iterator.hasNext()) return;

        T first = iterator.next();
        while (iterator.hasNext()) {
            T second = iterator.next();
            consumer.accept(first, second);
            first = second;
        }

        consumer.accept(first, null); // Handle last element with null
    }
}