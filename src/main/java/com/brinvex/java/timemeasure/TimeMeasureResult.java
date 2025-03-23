package com.brinvex.java.timemeasure;

import java.time.Duration;

public record TimeMeasureResult<T>(T result, Duration duration) {
    @Override
    public String toString() {
        return "T[ms]:" + duration.toMillis() + "/R:" + result;
    }
}
