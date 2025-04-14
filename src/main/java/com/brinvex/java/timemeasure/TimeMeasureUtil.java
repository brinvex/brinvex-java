package com.brinvex.java.timemeasure;

import com.brinvex.java.ThrowingRunnable;
import com.brinvex.java.ThrowingSupplier;

import java.time.Duration;
import java.util.function.Supplier;

public class TimeMeasureUtil {

    public static <T, E extends Exception> TimeMeasureResult<T> measureThrowing(ThrowingSupplier<T, E> task) throws TimeMeasureException {
        long start = System.nanoTime();
        T result;
        try {
            result = task.get();
            long end = System.nanoTime();
            return new TimeMeasureResult<>(result, Duration.ofNanos(end - start));
        } catch (Exception e) {
            long end = System.nanoTime();
            throw new TimeMeasureException(e, Duration.ofNanos(end - start));
        }
    }

    public static <E extends Exception> Duration measureThrowing(ThrowingRunnable<E> task) throws TimeMeasureException {
        long start = System.nanoTime();
        try {
            task.run();
            long end = System.nanoTime();
            return Duration.ofNanos(end - start);
        } catch (Exception e) {
            long end = System.nanoTime();
            throw new TimeMeasureException(e, Duration.ofNanos(end - start));
        }
    }

    public static <T> TimeMeasureResult<T> measure(Supplier<T> task) {
        long start = System.nanoTime();
        T result;
        try {
            result = task.get();
            long end = System.nanoTime();
            return new TimeMeasureResult<>(result, Duration.ofNanos(end - start));
        } catch (Exception e) {
            long end = System.nanoTime();
            throw new TimeMeasureRuntimeException(e, Duration.ofNanos(end - start));
        }
    }

    public static Duration measure(Runnable task) {
        long start = System.nanoTime();
        try {
            task.run();
            long end = System.nanoTime();
            return Duration.ofNanos(end - start);
        } catch (Exception e) {
            long end = System.nanoTime();
            throw new TimeMeasureRuntimeException(e, Duration.ofNanos(end - start));
        }
    }

}
