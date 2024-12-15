package com.brinvex.java;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class NullUtil {

    public static <T> T coalesce(T object1, T object2) {
        if (object1 != null) {
            return object1;
        }
        return object2;
    }

    public static <T> T coalesce(T object1, T object2, T object3) {
        if (object1 != null) {
            return object1;
        }
        if (object2 != null) {
            return object2;
        }
        return object3;
    }

    public static <T> T coalesce(T object1, Supplier<? extends T> object2) {
        if (object1 != null) {
            return object1;
        }
        return object2.get();
    }

    public static <I, O> O nullSafe(I input, Function<I, O> mapFnc) {
        return input == null ? null : mapFnc.apply(input);
    }

    public static <I, T1, O> O nullSafe(I input, Function<I, T1> mapFnc1, Function<T1, O> mapFnc2) {
        if (input == null) {
            return null;
        }
        T1 o1 = mapFnc1.apply(input);
        return o1 == null ? null : mapFnc2.apply(o1);
    }

    public static <I, T1, T2, O> O nullSafe(
            I input,
            Function<I, T1> mapFnc1,
            Function<T1, T2> mapFnc2,
            Function<T2, O> mapFnc3
    ) {
        if (input == null) {
            return null;
        }
        T1 o1 = mapFnc1.apply(input);
        if (o1 == null) {
            return null;
        }
        T2 o2 = mapFnc2.apply(o1);
        return o2 == null ? null : mapFnc3.apply(o2);
    }

    public static <I, T1, T2, T3, O> O nullSafe(
            I input,
            Function<I, T1> mapFnc1,
            Function<T1, T2> mapFnc2,
            Function<T2, T3> mapFnc3,
            Function<T3, O> mapFnc4
    ) {
        if (input == null) {
            return null;
        }
        T1 o1 = mapFnc1.apply(input);
        if (o1 == null) {
            return null;
        }
        T2 o2 = mapFnc2.apply(o1);
        if (o2 == null) {
            return null;
        }
        T3 o3 = mapFnc3.apply(o2);
        return o3 == null ? null : mapFnc4.apply(o3);
    }

    public static <I, T1, T2, O> Function<I, O> nullSafe(
            Function<I, T1> fnc1,
            Function<T1, T2> fnc2,
            Function<T2, O> fnc3
    ) {
        return input -> nullSafe(input, fnc1, fnc2, fnc3);
    }

    public static <T, V> Predicate<T> notNull(Function<? super T, ? extends V> valueExtractor) {
        return t -> valueExtractor.apply(t) != null;
    }

    public static <T, V> Predicate<T> isNull(Function<? super T, ? extends V> valueExtractor) {
        return t -> valueExtractor.apply(t) == null;
    }

    public static <E, C extends Collection<E>> C nullIfEmpty(C collection) {
        return collection == null || collection.isEmpty() ? null : collection;
    }

    /**
     * Returns null if two objects are equal, otherwise it returns the first object.
     */
    public static <T> T nullIf(T object1, T object2) {
        return Objects.equals(object1, object2) ? null : object1;
    }

    public static <T1, T2> Function<T1, T2> nullIf(Function<T1, T2> object1Fnc, T2 object2) {
        return o -> nullIf(object1Fnc.apply(o), object2);
    }
}
