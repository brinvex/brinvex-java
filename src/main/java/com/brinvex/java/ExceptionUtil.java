package com.brinvex.java;

import java.util.List;

public class ExceptionUtil {

    public static <E extends Exception> E getLastExceptionAndSuppressOthers(List<E> exceptions) {
        int size = exceptions.size();
        if (size == 0) {
            throw new IllegalArgumentException("Required non-empty list of exceptions");
        }
        if (size == 1) {
            return exceptions.get(0);
        }
        E mainException = exceptions.get(size - 1);
        for (int i = 0; i < size - 1; i++) {
            mainException.addSuppressed(exceptions.get(i));
        }
        return mainException;
    }

}
