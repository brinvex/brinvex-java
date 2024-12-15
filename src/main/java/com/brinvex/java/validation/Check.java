package com.brinvex.java.validation;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static java.math.BigDecimal.ZERO;

/**
 * <ul>
 *   <li>{@code Check} - If the condition is satisfied, returns {@code null}; otherwise, returns a message describing the condition violation.</li>
 *   <li>{@code Assert} - If the condition is satisfied, does nothing; otherwise, throws an {@code IllegalStateException} with a message describing the condition violation.</li>
 *   <li>{@code Validate} - If the condition is satisfied, does nothing; otherwise, throws an {@code IllegalArgumentException} with a message describing the condition violation.</li>
 * </ul>
 */
@SuppressWarnings({"SameParameterValue", "unused"})
public class Check {

    private static String format(Object o) {
        return (o instanceof String ? "'%s'" : "%s").formatted(o);
    }

    private static String nullSafeGet(Supplier<String> msgSupplier) {
        return msgSupplier == null ? null : msgSupplier.get();
    }

    public static String isTrue(Boolean expression) {
        return isTrue(expression, () -> "must be true");
    }

    public static String isTrue(Boolean expression, Supplier<String> msg) {
        if (expression != null) {
            if (!expression) {
                return nullSafeGet(msg);
            }
        }
        return null;
    }

    public static String isFalse(Boolean expression) {
        return isFalse(expression, () -> "must be false");
    }

    public static String isFalse(Boolean expression, Supplier<String> msg) {
        if (expression != null) {
            if (expression) {
                return nullSafeGet(msg);
            }
        }
        return null;
    }

    public static String notNull(Object input) {
        if (input == null) {
            return "must not be null";
        }
        return null;
    }

    public static String notNull(Object input, Supplier<String> msg) {
        if (input == null) {
            return nullSafeGet(msg);
        }
        return null;
    }

    public static String isNull(Object input) {
        return isNull(input, () -> "must be null, but given: %s".formatted(format(input)));
    }

    public static String isNull(Object input, Supplier<String> msg) {
        if (input != null) {
            return nullSafeGet(msg);
        }
        return null;
    }

    public static <T> String equal(T object1, T object2) {
        return equal(object1, object2, () -> "must be equal, but given: %s, %s".formatted(object1, object2));
    }

    public static <T> String equal(T object1, T object2, Supplier<String> msg) {
        boolean equal = Objects.equals(object1, object2);
        if (!equal) {
            return nullSafeGet(msg);
        }
        return null;
    }

    public static String empty(String input) {
        return empty(input, () -> "must be empty, but given: %s".formatted(format(input)));
    }

    public static String empty(String input, Supplier<String> msg) {
        if (input != null) {
            if (!input.isEmpty()) {
                return nullSafeGet(msg);
            }
        }
        return null;
    }

    /**
     * Similar to the {@link jakarta.validation.constraints.NotBlank}
     */
    public static String notNullNotBlank(String input) {
        return notNullNotBlank(input, () -> "must not be null and must not be blank, but given: %s".formatted(format(input)));
    }

    /**
     * Similar to the {@link jakarta.validation.constraints.NotBlank}
     */
    public static String notNullNotBlank(String input, Supplier<String> msg) {
        if (input == null || input.isBlank()) {
            return nullSafeGet(msg);
        }
        return null;
    }

    public static String zero(BigDecimal number) {
        return zero(number, () -> "must be zero, but given: %s".formatted(number));
    }

    public static String zero(BigDecimal number, Supplier<String> msg) {
        if (number != null) {
            if (number.compareTo(ZERO) != 0) {
                return nullSafeGet(msg);
            }
        }
        return null;
    }

    public static String positive(BigDecimal number) {
        return positive(number, () -> "must be greater than 0, but given: %s".formatted(number));
    }

    public static String positive(BigDecimal number, Supplier<String> msg) {
        if (number != null) {
            if (number.compareTo(ZERO) <= 0) {
                return nullSafeGet(msg);
            }
        }
        return null;
    }

    public static String positiveOrZero(BigDecimal number) {
        return positiveOrZero(number, () -> "must be greater than or equal to 0, but given: %s".formatted(number));
    }

    public static String positiveOrZero(BigDecimal number, Supplier<String> msg) {
        if (number != null) {
            if (number.compareTo(ZERO) < 0) {
                return nullSafeGet(msg);
            }
        }
        return null;
    }

    public static String negative(BigDecimal number) {
        return negative(number, () -> "must be less than 0, but given: %s".formatted(number));
    }

    public static String negative(BigDecimal number, Supplier<String> msg) {
        if (number != null) {
            if (number.compareTo(ZERO) >= 0) {
                return nullSafeGet(msg);
            }
        }
        return null;
    }

    public static String negativeOrZero(BigDecimal number) {
        return negativeOrZero(number, () -> "must be less than or equal to 0, but given: %s".formatted(number));
    }

    public static String negativeOrZero(BigDecimal number, Supplier<String> msg) {
        if (number != null) {
            if (number.compareTo(ZERO) > 0) {
                return nullSafeGet(msg);
            }
        }
        return null;
    }

    public static String equal(BigDecimal number1, BigDecimal number2) {
        return equal(number1, number2, () -> "must be equal, but given: %s, %s".formatted(number1, number2));
    }

    public static String equal(BigDecimal number1, BigDecimal number2, Supplier<String> msg) {
        if (number1 != null && number2 != null) {
            if (number1.compareTo(number2) != 0) {
                return nullSafeGet(msg);
            }
        } else if (number1 != null || number2 != null) {
            return nullSafeGet(msg);
        }
        return null;
    }

    public static String matches(String input, Pattern pattern) {
        return matches(input, pattern, () -> "must match '%s', but given: '%s'".formatted(pattern, input));
    }

    public static String matches(String input, Pattern pattern, Supplier<String> msg) {
        if (input != null) {
            boolean matches = pattern.matcher(input).matches();
            if (!matches) {
                return nullSafeGet(msg);
            }
        }
        return null;
    }

    public static String emptyOrMatches(String input, Pattern pattern) {
        return emptyOrMatches(input, pattern, () -> "must be empty or must match '%s', but given: '%s'".formatted(pattern, input));
    }

    public static String emptyOrMatches(String input, Pattern pattern, Supplier<String> msg) {
        if (input != null) {
            if (!input.isEmpty()) {
                boolean matches = pattern.matcher(input).matches();
                if (!matches) {
                    return nullSafeGet(msg);
                }
            }
        }
        return null;
    }
}
