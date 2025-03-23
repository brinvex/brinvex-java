package com.brinvex.java.timemeasure;

import java.time.Duration;

public class TimeMeasureException extends Exception {
    private final Duration duration;

    public TimeMeasureException(Throwable cause, Duration duration) {
        super("Execution failed after " + duration.toMillis() + " ms: " + cause, cause);
        this.duration = duration;
    }

    public Duration duration() {
        return duration;
    }
}
