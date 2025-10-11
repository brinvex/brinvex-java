package com.brinvex.java.concurrency;

public record TaskResult<R>(R value, Throwable error) {

    public static <R> TaskResult<R> success(R value) {
        return new TaskResult<>(value, null);
    }

    public static <R> TaskResult<R> failure(Throwable error) {
        return new TaskResult<>(null, error);
    }

    public boolean isSuccess() {
        return error == null;
    }
}