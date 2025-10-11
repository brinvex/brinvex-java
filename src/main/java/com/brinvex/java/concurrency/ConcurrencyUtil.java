package com.brinvex.java.concurrency;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ConcurrencyUtil {

    public static <R> List<TaskResult<R>> executeAllWithVirtualThreads(
            Collection<Callable<R>> tasks,
            Duration taskTimeout
    ) throws InterruptedException {

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<R>> futures = executor.invokeAll(tasks, taskTimeout.toMillis(), TimeUnit.MILLISECONDS);
            List<TaskResult<R>> results = new ArrayList<>(tasks.size());

            for (Future<R> future : futures) {
                try {
                    results.add(TaskResult.success(future.get()));
                } catch (CancellationException e) {
                    results.add(TaskResult.failure(new TimeoutException("Task timed out after %s".formatted(taskTimeout))));
                } catch (ExecutionException e) {
                    results.add(TaskResult.failure(e.getCause()));
                }
            }

            return results;
        }
    }
}
