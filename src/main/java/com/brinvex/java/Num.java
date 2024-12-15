package com.brinvex.java;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Comparator;

import static java.math.RoundingMode.HALF_UP;

public class Num {

    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;
    public static final MathContext MATH_CTX_P8_HALF_UP = new MathContext(8, HALF_UP);
    public static final MathContext MATH_CTX_P12_HALF_UP = new MathContext(12, HALF_UP);
    public static final MathContext MATH_CTX_P16_HALF_UP = new MathContext(16, HALF_UP);

    public static final BigDecimal _100 = new BigDecimal("100");
    public static final BigDecimal _100$00 = new BigDecimal("100.00");
    public static final BigDecimal _1000 = new BigDecimal("1000");
    public static final BigDecimal _1000$00 = new BigDecimal("1000.00");
    public static final BigDecimal MINUS_1 = new BigDecimal("-1");

    public static BigDecimal of(String s) {
        return s == null ? null : new BigDecimal(s);
    }

    public static boolean isNullOrZero(BigDecimal bd) {
        return bd == null || bd.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isZero(BigDecimal bd) {
        return bd.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isNullOrPositive(BigDecimal bd) {
        return bd == null || bd.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isPositive(BigDecimal bd) {
        return bd.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNullOrNonNegative(BigDecimal bd) {
        return bd == null || bd.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean isNullOrNegative(BigDecimal bd) {
        return bd == null || bd.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isNegative(BigDecimal bd) {
        return bd.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isNullOrNonPositive(BigDecimal bd) {
        return bd == null || bd.compareTo(BigDecimal.ZERO) <= 0;
    }

    public static boolean isNonPositive(BigDecimal bd) {
        return bd.compareTo(BigDecimal.ZERO) <= 0;
    }

    public static boolean nullOrEqual(
            BigDecimal bd1,
            BigDecimal bd2,
            int scale,
            RoundingMode roundingMode
    ) {
        if (bd1 == null && bd2 == null) {
            return true;
        }
        if (bd1 == null || bd2 == null) {
            return false;
        }
        return equal(bd1, bd2, scale, roundingMode);
    }

    public static boolean nullOrEqual(BigDecimal bd1, BigDecimal bd2, int scale) {
        return nullOrEqual(bd1, bd2, scale, DEFAULT_ROUNDING_MODE);
    }

    public static boolean equal(BigDecimal bd1, BigDecimal bd2, int scale, RoundingMode roundingMode) {
        bd1 = bd1.setScale(scale, roundingMode);
        bd2 = bd2.setScale(scale, roundingMode);
        return bd1.compareTo(bd2) == 0;
    }

    public static boolean equal(BigDecimal bd1, BigDecimal bd2, int scale) {
        return equal(bd1, bd2, scale, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal avg(Collection<BigDecimal> decimals, int scale) {
        return avg(decimals, scale, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal avg(Collection<BigDecimal> decimals, int scale, RoundingMode roundingMode) {
        int size = decimals.size();
        return switch (size) {
            case 0 -> null;
            case 1 -> decimals.iterator().next().setScale(scale, roundingMode);
            default -> decimals
                    .stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(size), scale, roundingMode);
        };
    }

    public static BigDecimal intAvg(Collection<Integer> decimals, int scale) {
        return intAvg(decimals, scale, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal intAvg(Collection<Integer> integers, int scale, RoundingMode roundingMode) {
        int size = integers.size();
        return switch (size) {
            case 0 -> null;
            case 1 -> BigDecimal.valueOf(integers.iterator().next()).setScale(scale, roundingMode);
            default -> BigDecimal.valueOf(integers
                            .stream()
                            .reduce(0, Integer::sum))
                    .divide(BigDecimal.valueOf(size), scale, roundingMode);
        };
    }

    public static BigDecimal min(Collection<BigDecimal> decimals) {
        return switch (decimals.size()) {
            case 0 -> null;
            case 1 -> decimals.iterator().next();
            default -> decimals
                    .stream()
                    .min(Comparator.naturalOrder())
                    .get();
        };
    }

    public static BigDecimal max(Collection<BigDecimal> decimals) {
        return switch (decimals.size()) {
            case 0 -> null;
            case 1 -> decimals.iterator().next();
            default -> decimals
                    .stream()
                    .max(Comparator.naturalOrder())
                    .get();
        };
    }

    public static BigDecimal divide(
            BigDecimal bd1,
            BigDecimal bd2,
            int scale,
            RoundingMode roundingMode
    ) {
        return bd1.divide(bd2, scale, roundingMode);
    }

    public static BigDecimal divide(
            BigDecimal bd1,
            BigDecimal bd2,
            int scale
    ) {
        return divide(bd1, bd2, scale, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal setScale(BigDecimal bd, int newScale, RoundingMode roundingMode) {
        if (bd == null) {
            return null;
        }
        return bd.setScale(newScale, roundingMode);
    }

    public static BigDecimal setScale(BigDecimal bd, int newScale) {
        return setScale(bd, newScale, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal setScale0(BigDecimal bd) {
        return setScale(bd, 0, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal setScale1(BigDecimal bd) {
        return setScale(bd, 2, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal setScale2(BigDecimal bd) {
        return setScale(bd, 2, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal setScale3(BigDecimal bd) {
        return setScale(bd, 3, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal setScale4(BigDecimal bd) {
        return setScale(bd, 4, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal setScale6(BigDecimal bd) {
        return setScale(bd, 6, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal setScale8(BigDecimal bd) {
        return setScale(bd, 8, DEFAULT_ROUNDING_MODE);
    }

    public static BigInteger toBigInteger(BigDecimal bd) {
        return bd == null ? null : bd.toBigInteger();
    }

    public static String format(Number bd, NumberFormat format) {
        return bd == null ? null : format.format(bd);
    }

    public static String format(Number bd, NumberFormat format, String nullStr) {
        return bd == null ? nullStr : format.format(bd);
    }

    public static BigDecimal distance(BigDecimal bd1, BigDecimal bd2) {
        return bd1.subtract(bd2).abs();
    }

    public static BigDecimal distance(BigDecimal bd1, BigDecimal bd2, int scale) {
        return distance(bd1, bd2, scale, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal distance(BigDecimal bd1, BigDecimal bd2, int scale, RoundingMode roundingMode) {
        return setScale(bd1.subtract(bd2).abs(), scale, roundingMode);
    }

    public static BigDecimal relativeChange(BigInteger oldValue, BigInteger newValue, int scale) {
        return relativeChange(new BigDecimal(oldValue), new BigDecimal(newValue), scale, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal relativeChange(long oldValue, long newValue, int scale) {
        return relativeChange(new BigDecimal(oldValue), new BigDecimal(newValue), scale, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal relativeChange(BigDecimal oldValue, BigDecimal newValue, int scale) {
        return relativeChange(oldValue, newValue, scale, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal relativeChange(
            BigDecimal oldValue,
            BigDecimal newValue,
            int scale,
            RoundingMode roundingMode
    ) {
        return newValue.subtract(oldValue).divide(oldValue, scale, roundingMode);
    }

    public static BigDecimal relativeChangeInPct(BigInteger oldValue, BigInteger newValue, int scale) {
        return relativeChangeInPct(new BigDecimal(oldValue), new BigDecimal(newValue), scale, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal relativeChangeInPct(long oldValue, long newValue, int scale) {
        return relativeChangeInPct(new BigDecimal(oldValue), new BigDecimal(newValue), scale, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal relativeChangeInPct(BigDecimal oldValue, BigDecimal newValue, int scale) {
        return relativeChangeInPct(oldValue, newValue, scale, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal relativeChangeInPct(
            BigDecimal oldValue,
            BigDecimal newValue,
            int scale,
            RoundingMode roundingMode
    ) {
        return ((newValue.subtract(oldValue)).multiply(_100$00)).divide(oldValue, scale, roundingMode);
    }

    public static Double round(Double number, int scale) {
        return number == null ? null : BigDecimal.valueOf(number).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static Double round2(Double number) {
        return round(number, 2);
    }

    public static BigDecimal multiplyP8(BigDecimal d1, BigDecimal d2) {
        return d1.multiply(d2, MATH_CTX_P8_HALF_UP);
    }

    public static BigDecimal multiplyP12(BigDecimal d1, BigDecimal d2) {
        return d1.multiply(d2, MATH_CTX_P12_HALF_UP);
    }

    public static BigDecimal multiplyP16(BigDecimal d1, BigDecimal d2) {
        return d1.multiply(d2, MATH_CTX_P16_HALF_UP);
    }

}
