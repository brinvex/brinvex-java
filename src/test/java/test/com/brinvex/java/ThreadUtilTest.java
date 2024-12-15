package test.com.brinvex.java;

import com.brinvex.java.ThreadUtil;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadUtilTest {

    @Test
    public void executeAndSleep_fast() throws InterruptedException {
        Instant testStart = Instant.now();
        Duration taskMinDuration = Duration.ofSeconds(2);
        ThreadUtil.executeAndSleep(taskMinDuration, () -> {
            //no-op
        });
        Instant testEnd = Instant.now();
        Duration testDuration = Duration.between(testStart, testEnd);
        assertTrue(testDuration.compareTo(taskMinDuration) > 0);

    }

    @Test
    public void executeAndSleep_slow() throws InterruptedException {
        Instant testStart = Instant.now();
        Duration taskMinDuration = Duration.ofSeconds(2);
        Duration taskDuration = Duration.ofSeconds(3);
        ThreadUtil.executeAndSleep(taskMinDuration, () -> {
            try {
                Thread.sleep(taskDuration.toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Instant testEnd = Instant.now();
        Duration testDuration = Duration.between(testStart, testEnd);
        assertTrue(testDuration.compareTo(taskMinDuration) > 0);
        assertTrue(testDuration.compareTo(taskDuration) > 0);

    }

    @Test
    public void executeAndSleep_interrupted() {
        Instant testStart = Instant.now();
        Duration taskMinDuration = Duration.ofSeconds(2);
        Thread t = new Thread(() -> {
            try {
                ThreadUtil.executeAndSleep(taskMinDuration, () -> {
                    throw new RuntimeException("e1");
                });
            } catch (InterruptedException e) {
                Throwable[] suppressed = e.getSuppressed();
                assertEquals("e1", suppressed[0].getMessage());
            }
        });
        t.start();
        t.interrupt();

        Instant testEnd = Instant.now();
        Duration testDuration = Duration.between(testStart, testEnd);
        assertTrue(testDuration.compareTo(taskMinDuration) < 0);

    }

}
