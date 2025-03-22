package com.brinvex.java;

@FunctionalInterface
public interface ThrowingRunnable<E extends Exception> {
    void run() throws E;
}