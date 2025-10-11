package test.com.brinvex.java;

import com.brinvex.java.concurrency.ConcurrencyUtil;
import com.brinvex.java.concurrency.TaskResult;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrencyUtilTest {

    @Test
    void testAllSuccess() throws InterruptedException {
        List<Callable<String>> tasks = List.of(
                () -> "A",
                () -> "B",
                () -> "C"
        );

        List<TaskResult<String>> results = ConcurrencyUtil.executeAllWithVirtualThreads(tasks, Duration.ofSeconds(1));

        assertEquals(3, results.size());
        for (TaskResult<String> r : results) {
            assertTrue(r.isSuccess(), "Task should succeed");
            assertNotNull(r.value());
            assertNull(r.error());
        }
    }

    @Test
    void testSomeFailures() throws InterruptedException {
        List<Callable<String>> tasks = List.of(
                () -> "A",
                () -> {
                    throw new IllegalStateException("Fail");
                },
                () -> "C"
        );

        List<TaskResult<String>> results = ConcurrencyUtil.executeAllWithVirtualThreads(tasks, Duration.ofSeconds(1));

        assertEquals(3, results.size());

        assertTrue(results.get(0).isSuccess());
        assertEquals("A", results.get(0).value());

        assertFalse(results.get(1).isSuccess());
        assertInstanceOf(IllegalStateException.class, results.get(1).error());

        assertTrue(results.get(2).isSuccess());
        assertEquals("C", results.get(2).value());
    }

    @Test
    void testTimeout() throws InterruptedException {
        List<Callable<String>> tasks = List.of(
                () -> {
                    Thread.sleep(500);
                    return "A";
                },
                () -> {
                    Thread.sleep(2000);
                    return "B";
                } // will time out
        );

        List<TaskResult<String>> results = ConcurrencyUtil.executeAllWithVirtualThreads(tasks, Duration.ofMillis(1000));

        assertEquals(2, results.size());

        assertTrue(results.get(0).isSuccess());
        assertEquals("A", results.get(0).value());

        assertFalse(results.get(1).isSuccess());
        assertInstanceOf(TimeoutException.class, results.get(1).error());
    }

    @Test
    void testEmptyTasks() throws InterruptedException {
        List<TaskResult<String>> results = ConcurrencyUtil.executeAllWithVirtualThreads(List.of(), Duration.ofSeconds(1));
        assertTrue(results.isEmpty(), "Result list should be empty");
    }
}
