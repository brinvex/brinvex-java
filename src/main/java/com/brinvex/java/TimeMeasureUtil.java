package com.brinvex.java;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class TimeMeasureUtil {

    public record TimeMeasureResult<T>(T result, Duration duration) {
        @Override
        public String toString() {
            return "T[ms]:" + duration.toMillis() + "/R:" + result;
        }
    }

    public static class TimeMeasureException extends Exception {
        private final Duration duration;

        public TimeMeasureException(Throwable cause, Duration duration) {
            super("Execution failed after " + duration.toMillis() + " ms: " + cause, cause);
            this.duration = duration;
        }

        public Duration duration() {
            return duration;
        }
    }

    public static class TimeMeasureRuntimeException extends RuntimeException {
        private final Duration duration;

        public TimeMeasureRuntimeException(Throwable cause, Duration duration) {
            super("Execution failed after " + duration.toMillis() + " ms: " + cause, cause);
            this.duration = duration;
        }

        public Duration duration() {
            return duration;
        }
    }

    public static <T> TimeMeasureResult<T> measureThrowing(Callable<T> task) throws TimeMeasureException {
        Instant start = Instant.now();
        T result;
        try {
            result = task.call();
            Instant end = Instant.now();
            return new TimeMeasureResult<>(result, Duration.between(start, end));
        } catch (Exception e) {
            Instant end = Instant.now();
            throw new TimeMeasureException(e, Duration.between(start, end));
        }
    }

    public static Duration measureThrowing(ThrowingRunnable task) throws TimeMeasureException {
        Instant start = Instant.now();
        try {
            task.run();
            Instant end = Instant.now();
            return Duration.between(start, end);
        } catch (Exception e) {
            Instant end = Instant.now();
            throw new TimeMeasureException(e, Duration.between(start, end));
        }
    }

    public static <T> TimeMeasureResult<T> measure(Supplier<T> task) {
        Instant start = Instant.now();
        T result;
        try {
            result = task.get();
            Instant end = Instant.now();
            return new TimeMeasureResult<>(result, Duration.between(start, end));
        } catch (Exception e) {
            Instant end = Instant.now();
            throw new TimeMeasureRuntimeException(e, Duration.between(start, end));
        }
    }

    public static Duration measure(Runnable task) {
        Instant start = Instant.now();
        try {
            task.run();
            Instant end = Instant.now();
            return Duration.between(start, end);
        } catch (Exception e) {
            Instant end = Instant.now();
            throw new TimeMeasureRuntimeException(e, Duration.between(start, end));
        }
    }

}
