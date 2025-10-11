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

    /**
     * @deprecated Use {@link com.brinvex.java.concurrency.ConcurrencyUtil#executeAllWithVirtualThreads(Collection, Duration)}
     */
    public static <R> List<R> invokeAllAndGetResults(
            int nThreads,
            Collection<Callable<R>> tasks,
            Duration taskTimeout
    ) throws InterruptedException, ExecutionException, TimeoutException {
        int nTasks = tasks.size();
        long taskTimeoutMillis = taskTimeout.toMillis();

        ExecutorService executorService = null;
        try {
            executorService = Executors.newFixedThreadPool(nThreads);

            List<Future<R>> futures = executorService.invokeAll(tasks);
            List<R> results = new ArrayList<>(nTasks);
            for (Future<R> future : futures) {
                R taskResult = future.get(taskTimeoutMillis, TimeUnit.MILLISECONDS);
                results.add(taskResult);
            }
            return results;
        } finally {
            if (executorService != null) {
                executorService.shutdown();
                try {
                    if (!executorService.awaitTermination((long) nTasks * taskTimeoutMillis, TimeUnit.MILLISECONDS)) {
                        executorService.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    executorService.shutdownNow();
                }
            }
        }
    }

    /**
     * @deprecated Use {@link com.brinvex.java.concurrency.ConcurrencyUtil#executeAllWithVirtualThreads(Collection, Duration)}
     */
    public static <T> void execute(
            int nThreads,
            Collection<Runnable> tasks
    ) {
        ExecutorService executorService = null;
        try {
            executorService = Executors.newFixedThreadPool(nThreads);
            for (Runnable task : tasks) {
                executorService.execute(task);
            }
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }

    /**
     * @deprecated
     */
    public static void executeAndSleep(Duration minDuration, Runnable task) throws InterruptedException {
        Instant start = Instant.now();
        RuntimeException exception = null;
        try {
            task.run();
        } catch (RuntimeException e) {
            exception = e;
        }
        {
            Instant finish = Instant.now();
            Duration taskDuration = Duration.between(start, finish);
            Duration sleepDuration = minDuration.minus(taskDuration);
            long sleepMillis = sleepDuration.toMillis();
            if (sleepMillis > 0) {
                try {
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException e) {
                    if (exception != null) {
                        e.addSuppressed(exception);
                    }
                    throw e;
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
    }
}
