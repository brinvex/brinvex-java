package com.brinvex.java.validation;

import java.math.BigDecimal;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * <ul>
 *   <li>{@code Check} - If the condition is satisfied, returns {@code null}; otherwise, returns a message describing the condition violation.</li>
 *   <li>{@code Assert} - If the condition is satisfied, does nothing; otherwise, throws an {@code IllegalStateException} with a message describing the condition violation.</li>
 *   <li>{@code Validate} - If the condition is satisfied, does nothing; otherwise, throws an {@code IllegalArgumentException} with a message describing the condition violation.</li>
 * </ul>
 */
@SuppressWarnings({"SameParameterValue", "unused"})
public class Validate {

    private static void throwIfNotNull(String violationMsg) {
        if (violationMsg != null) {
            throw new IllegalArgumentException(violationMsg);
        }
    }

    public static void isTrue(Boolean expression) {
        throwIfNotNull(Check.isTrue(expression));
    }

    public static void isTrue(Boolean expression, Supplier<String> msg) {
        throwIfNotNull(Check.isTrue(expression, msg));
    }

    public static void isFalse(Boolean expression) {
        throwIfNotNull(Check.isFalse(expression));
    }

    public static void isFalse(Boolean expression, Supplier<String> msg) {
        throwIfNotNull(Check.isFalse(expression, msg));
    }

    public static void notNull(Object input) {
        throwIfNotNull(Check.notNull(input));
    }

    public static void notNull(Object input, Supplier<String> msg) {
        throwIfNotNull(Check.notNull(input, msg));
    }

    public static void isNull(Object input) {
        throwIfNotNull(Check.isNull(input));
    }

    public static void isNull(Object input, Supplier<String> msg) {
        throwIfNotNull(Check.isNull(input, msg));
    }

    public static <T> void equal(T object1, T object2) {
        throwIfNotNull(Check.equal(object1, object2));
    }

    public static <T> void equal(T object1, T object2, Supplier<String> msg) {
        throwIfNotNull(Check.equal(object1, object2, msg));
    }

    public static void empty(String input) {
        throwIfNotNull(Check.empty(input));
    }

    public static void empty(String input, Supplier<String> msg) {
        throwIfNotNull(Check.empty(input, msg));
    }

    /**
     * Similar to the {@link jakarta.validation.constraints.NotBlank}
     */
    public static void notNullNotBlank(String input) {
        throwIfNotNull(Check.notNullNotBlank(input));
    }

    /**
     * Similar to the {@link jakarta.validation.constraints.NotBlank}
     */
    public static void notNullNotBlank(String input, Supplier<String> msg) {
        throwIfNotNull(Check.notNullNotBlank(input, msg));
    }

    public static void zero(BigDecimal number) {
        throwIfNotNull(Check.zero(number));
    }

    public static void zero(BigDecimal number, Supplier<String> msg) {
        throwIfNotNull(Check.zero(number, msg));
    }

    public static void positive(BigDecimal number) {
        throwIfNotNull(Check.positive(number));
    }

    public static void positive(BigDecimal number, Supplier<String> msg) {
        throwIfNotNull(Check.positive(number, msg));
    }

    public static void positiveOrZero(BigDecimal number) {
        throwIfNotNull(Check.positiveOrZero(number));
    }

    public static void positiveOrZero(BigDecimal number, Supplier<String> msg) {
        throwIfNotNull(Check.positiveOrZero(number, msg));
    }

    public static void negative(BigDecimal number) {
        throwIfNotNull(Check.negative(number));
    }

    public static void negative(BigDecimal number, Supplier<String> msg) {
        throwIfNotNull(Check.negative(number, msg));
    }

    public static void negativeOrZero(BigDecimal number) {
        throwIfNotNull(Check.negativeOrZero(number));
    }

    public static void negativeOrZero(BigDecimal number, Supplier<String> msg) {
        throwIfNotNull(Check.negativeOrZero(number, msg));
    }

    public static void equal(BigDecimal number1, BigDecimal number2) {
        throwIfNotNull(Check.equal(number1, number2));
    }

    public static void equal(BigDecimal number1, BigDecimal number2, Supplier<String> msg) {
        throwIfNotNull(Check.equal(number1, number2, msg));
    }

    public static void matches(String input, Pattern pattern) {
        throwIfNotNull(Check.matches(input, pattern));
    }

    public static void matches(String input, Pattern pattern, Supplier<String> msg) {
        throwIfNotNull(Check.matches(input, pattern, msg));
    }

    public static void emptyOrMatches(String input, Pattern pattern) {
        throwIfNotNull(Check.emptyOrMatches(input, pattern));
    }

    public static void emptyOrMatches(String input, Pattern pattern, Supplier<String> msg) {
        throwIfNotNull(Check.emptyOrMatches(input, pattern, msg));
    }
}
