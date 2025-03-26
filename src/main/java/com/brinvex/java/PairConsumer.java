package com.brinvex.java;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface PairConsumer<T> extends BiConsumer<T, T> {

    void accept(T first, T second);

}