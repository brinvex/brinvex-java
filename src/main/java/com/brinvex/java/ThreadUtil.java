package com.brinvex.java;

import java.time.Duration;
import java.util.function.Function;

public class ThreadUtil {

    public static void sleep(Duration duration, Function<InterruptedException, RuntimeException> interruptedExceptionWrapper) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw interruptedExceptionWrapper.apply(e);
        }
    }

    public static void sleep(Duration duration) {
        sleep(duration, interruptedException -> new RuntimeException("Thread interrupted while trying to sleep %s".formatted(duration), interruptedException));
    }

}
