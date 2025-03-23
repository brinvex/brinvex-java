package com.brinvex.java.timemeasure;

import com.brinvex.java.ThrowingRunnable;
import com.brinvex.java.ThrowingSupplier;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public class TimeMeasureUtil {

    public static <T, E extends Exception> TimeMeasureResult<T> measureThrowing(ThrowingSupplier<T, E> task) throws TimeMeasureException {
        Instant start = Instant.now();
        T result;
        try {
            result = task.get();
            Instant end = Instant.now();
            return new TimeMeasureResult<>(result, Duration.between(start, end));
        } catch (Exception e) {
            Instant end = Instant.now();
            throw new TimeMeasureException(e, Duration.between(start, end));
        }
    }

    public static <E extends Exception> Duration measureThrowing(ThrowingRunnable<E> task) throws TimeMeasureException {
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
