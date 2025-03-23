package com.brinvex.java.timemeasure;

import java.time.Duration;

public class TimeMeasureRuntimeException extends RuntimeException {
    private final Duration duration;

    public TimeMeasureRuntimeException(Throwable cause, Duration duration) {
        super("Execution failed after " + duration.toMillis() + " ms: " + cause, cause);
        this.duration = duration;
    }

    public Duration duration() {
        return duration;
    }
}
