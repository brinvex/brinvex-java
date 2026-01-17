package com.brinvex.java;

import java.util.Collection;
import java.util.Iterator;
import java.util.SequencedCollection;

public class ExceptionUtil {

    public static <THROWABLE extends Throwable> THROWABLE getFirstAndSuppressOthers(Collection<THROWABLE> exceptions) {
        Iterator<THROWABLE> it = exceptions.iterator();
        THROWABLE mainException = it.next();
        while (it.hasNext()) {
            mainException.addSuppressed(it.next());
        }
        return mainException;
    }

    public static <THROWABLE extends Throwable> THROWABLE getLastAndSuppressOthers(SequencedCollection<THROWABLE> exceptions) {
        THROWABLE mainException = exceptions.getLast();
        for (THROWABLE exception : exceptions) {
            if (exception != mainException) {
                mainException.addSuppressed(exception);
            }
        }
        return mainException;
    }
}
