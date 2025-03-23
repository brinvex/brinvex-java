package test.com.brinvex.java;

import com.brinvex.java.timemeasure.TimeMeasureException;
import com.brinvex.java.timemeasure.TimeMeasureResult;
import com.brinvex.java.timemeasure.TimeMeasureUtil;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeMeasureUtilTest {

    @Test
    void measureThrowingRunnable() throws TimeMeasureException {
        Duration dur = TimeMeasureUtil.measureThrowing(() -> Thread.sleep(500));
        assertTrue(dur.toMillis() >= 500, dur.toString());
    }

    @Test
    void measureThrowingCallable() throws TimeMeasureException {
        TimeMeasureResult<String> timeMeasureResult = TimeMeasureUtil.measureThrowing(() -> {
            Thread.sleep(500);
            return "ok";
        });
        assertEquals("ok", timeMeasureResult.result());
        assertTrue(timeMeasureResult.duration().toMillis() >= 500, timeMeasureResult.toString());
    }

    @Test
    void measureRunnable() {
        Duration dur = TimeMeasureUtil.measure(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        assertTrue(dur.toMillis() >= 500, dur.toString());
    }

    @Test
    void measureCallable() {
        TimeMeasureResult<String> timeMeasureResult = TimeMeasureUtil.measure(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "ok";
        });
        assertEquals("ok", timeMeasureResult.result());
        assertTrue(timeMeasureResult.duration().toMillis() >= 500, timeMeasureResult.toString());
    }
}
