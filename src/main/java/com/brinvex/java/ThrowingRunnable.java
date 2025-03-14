package com.brinvex.java;

@FunctionalInterface
public interface ThrowingRunnable {
    void run() throws Exception;
}