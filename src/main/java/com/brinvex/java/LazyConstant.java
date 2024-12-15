package com.brinvex.java;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * //todo 4 Make it sealed
 * Inspired by <a href="https://www.baeldung.com/java-lambda-lazy-field-initialization">www.baeldung.com/java-lambda-lazy-field-initialization</a>
 */
public abstract class LazyConstant<T> implements Supplier<T> {

    public static <T> LazyConstant<T> threadSafe(Supplier<T> supplier) {
        return new ThreadSafeLazyConstant<>(supplier);
    }

    public static <T> LazyConstant<T> nonThreadSafe(Supplier<T> supplier) {
        return new NonThreadSafeLazyConstant<>(supplier);
    }

    private static final class ThreadSafeLazyConstant<T> extends LazyConstant<T> {

        private final Supplier<T> supplier;

        private final AtomicReference<Object> storedValueRef = new AtomicReference<>();

        private static final Object nullSubstitute = new Object();

        public ThreadSafeLazyConstant(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
            Object storedValue = storedValueRef.get();
            if (storedValue == null) {
                synchronized (storedValueRef) {
                    storedValue = storedValueRef.get();
                    if (storedValue == null) {
                        T value = supplier.get();
                        storedValueRef.set(value == null ? nullSubstitute : value);
                        return value;
                    } else {
                        if (storedValue == nullSubstitute) {
                            return null;
                        }
                        @SuppressWarnings({"ReassignedVariable", "unchecked"})
                        T typedStoredValue = (T) storedValue;
                        return typedStoredValue;
                    }
                }
            } else {
                if (storedValue == nullSubstitute) {
                    return null;
                }
                @SuppressWarnings({"ReassignedVariable", "unchecked"})
                T typedStoredValue = (T) storedValue;
                return typedStoredValue;
            }
        }
    }

    private static final class NonThreadSafeLazyConstant<T> extends LazyConstant<T> {

        private final Supplier<T> supplier;

        private Object storedValue = null;

        private static final Object nullSubstitute = new Object();

        public NonThreadSafeLazyConstant(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
            if (storedValue == null) {
                T value = supplier.get();
                storedValue = value == null ? nullSubstitute : value;
                return value;
            } else {
                if (storedValue == nullSubstitute) {
                    return null;
                }
                @SuppressWarnings({"unchecked"})
                T typedStoredValue = (T) storedValue;
                return typedStoredValue;
            }
        }
    }

}
