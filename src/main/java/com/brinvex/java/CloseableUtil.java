package com.brinvex.java;

import java.util.function.Consumer;
import java.util.function.Function;

public class CloseableUtil {

    public static <T extends AutoCloseable, R> R applyAndClose(T autoCloseableResource, Function<T, R> action) {
        Throwable mainException = null;
        try {
            return action.apply(autoCloseableResource);
        } catch (Throwable e) {
            mainException = e;
            throw e;
        } finally {
            closeResource(autoCloseableResource, mainException);
        }
    }

    public static <T extends AutoCloseable> void useAndClose(T autoCloseableResource, Consumer<T> action) {
        Throwable mainException = null;
        try {
            action.accept(autoCloseableResource);
        } catch (Throwable e) {
            mainException = e;
            throw e;
        } finally {
            closeResource(autoCloseableResource, mainException);
        }
    }

    public static <T extends AutoCloseable, R, E extends Exception> R applyAndCloseThrowing(T autoCloseableResource, ThrowingFunction<T, R, E> action) throws E {
        Throwable mainException = null;
        try {
            return action.apply(autoCloseableResource);
        } catch (Throwable e) {
            mainException = e;
            throw e;
        } finally {
            closeResource(autoCloseableResource, mainException);
        }
    }

    public static <T extends AutoCloseable, E extends Exception> void useAndCloseThrowing(T autoCloseableResource, ThrowingConsumer<T, E> action) throws E {
        Throwable mainException = null;
        try {
            action.accept(autoCloseableResource);
        } catch (Throwable e) {
            mainException = e;
            throw e;
        } finally {
            closeResource(autoCloseableResource, mainException);
        }
    }

    private static <T extends AutoCloseable> void closeResource(T autoCloseableResource, Throwable mainException) {
        try {
            if (autoCloseableResource != null) {
                autoCloseableResource.close();
            }
        } catch (Throwable closeException) {
            if (mainException != null) {
                mainException.addSuppressed(closeException);
            } else if (closeException instanceof RuntimeException) {
                throw (RuntimeException) closeException;
            } else if (closeException instanceof Error) {
                throw (Error) closeException;
            } else {
                throw new RuntimeException("Exception caught while closing resource: " + closeException);
            }
        }
    }


}