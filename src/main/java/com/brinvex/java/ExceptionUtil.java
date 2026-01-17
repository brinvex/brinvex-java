package com.brinvex.java;

import java.util.List;

public class ExceptionUtil {

    public static <THROWABLE extends Throwable> THROWABLE getFirstThrowableAndSuppressOthers(List<THROWABLE> exceptions) {
        int size = exceptions.size();
        if (size == 0) {
            throw new IllegalArgumentException("Required non-empty list of exceptions");
        }
        THROWABLE mainException = exceptions.getFirst();
        for (int i = 0; i < size - 1; i++) {
            mainException.addSuppressed(exceptions.get(i));
        }
        return mainException;
    }

    public static <THROWABLE extends Throwable> THROWABLE getLastThrowableAndSuppressOthers(List<THROWABLE> exceptions) {
        int size = exceptions.size();
        if (size == 0) {
            throw new IllegalArgumentException("Required non-empty list of exceptions");
        }
        THROWABLE mainException = exceptions.getLast();
        for (int i = 0; i < size - 1; i++) {
            mainException.addSuppressed(exceptions.get(i));
        }
        return mainException;
    }


}
