package com.brinvex.java;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
