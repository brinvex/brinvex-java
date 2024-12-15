package com.brinvex.java;

import java.util.function.Function;
import java.util.function.Predicate;

public class LambdaUtil {

    public static <I, T, O> Function<I, O> chain(Function<I, T> fnc1, Function<T, O> fnc2) {
        return i -> fnc2.apply(fnc1.apply(i));
    }

    public static <I, T1, T2, O> Function<I, O> chain(
            Function<I, T1> fnc1,
            Function<T1, T2> fnc2,
            Function<T2, O> fnc3
    ) {
        return i -> fnc3.apply(fnc2.apply(fnc1.apply(i)));
    }

    public static <I, T> Predicate<I> chain(Function<I, T> fnc1, Predicate<T> fnc2) {
        return i -> fnc2.test(fnc1.apply(i));
    }

    public static <I, T1, T2> Predicate<I> chain(
            Function<I, T1> fnc1,
            Function<T1, T2> fnc2,
            Predicate<T2> fnc3
    ) {
        return i -> fnc3.test(fnc2.apply(fnc1.apply(i)));
    }


}
