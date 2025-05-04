package com.brinvex.java;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static com.brinvex.java.DateUtil.maxDate;
import static com.brinvex.java.DateUtil.minDate;

public final class DateRange {

    private LocalDate startExcl;
    private final LocalDate startIncl;
    private LocalDate endIncl;
    private final LocalDate endExcl;

    public static DateRange dateRangeIncl(LocalDate startIncl, LocalDate endIncl) {
        return new DateRange(startIncl, endIncl == null ? null : endIncl.plusDays(1));
    }

    public static DateRange dateRangeStartExcl(LocalDate startExcl, LocalDate endIncl) {
        return new DateRange(startExcl == null ? null : startExcl.plusDays(1), endIncl == null ? null : endIncl.plusDays(1));
    }

    public static DateRange dateRangeEndExcl(LocalDate startIncl, LocalDate endExcl) {
        return new DateRange(startIncl, endExcl);
    }

    public static DateRange dateRangeExcl(LocalDate startExcl, LocalDate endExcl) {
        return new DateRange(startExcl == null ? null : startExcl.plusDays(1), endExcl);
    }

    /**
     * Parses the given ISO‑8601 date strings and returns an inclusive DateRange.
     */
    public static DateRange dateRangeIncl(String startIncl, String endIncl) {
        return dateRangeIncl(
                startIncl == null ? null : LocalDate.parse(startIncl),
                endIncl == null ? null : LocalDate.parse(endIncl)
        );
    }

    /**
     * Parses the given ISO‑8601 date strings and returns a DateRange with exclusive start.
     */
    public static DateRange dateRangeStartExcl(String startExcl, String endIncl) {
        return dateRangeStartExcl(
                startExcl == null ? null : LocalDate.parse(startExcl),
                endIncl == null ? null : LocalDate.parse(endIncl)
        );
    }

    /**
     * Parses the given ISO‑8601 date strings and returns a DateRange with exclusive end.
     */
    public static DateRange dateRangeEndExcl(String startIncl, String endExcl) {
        return dateRangeEndExcl(
                startIncl == null ? null : LocalDate.parse(startIncl),
                endExcl == null ? null : LocalDate.parse(endExcl)
        );
    }

    /**
     * Parses the given ISO‑8601 date strings and returns a fully exclusive DateRange.
     */
    public static DateRange dateRangeExcl(String startExcl, String endExcl) {
        return dateRangeExcl(
                startExcl == null ? null : LocalDate.parse(startExcl),
                endExcl == null ? null : LocalDate.parse(endExcl)
        );
    }

    /**
     * <br> [2025-03-17, 2025-03-18) OK
     * <br> [2025-03-17, 2025-03-17) OK zero-length date range
     * <br> [2025-03-17, 2025-03-16) Illegal
     */
    public DateRange(LocalDate startIncl, LocalDate endExcl) {
        if (startIncl != null && endExcl != null && startIncl.isAfter(endExcl)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date, given: %s, %s".formatted(startIncl, endExcl));
        }
        this.startIncl = startIncl;
        this.endExcl = endExcl;
    }

    public LocalDate startIncl() {
        return startIncl;
    }

    public LocalDate startExcl() {
        return startExcl == null ? startIncl == null ? null : (startExcl = startIncl.minusDays(1)) : startExcl;
    }

    public LocalDate endIncl() {
        return endIncl == null ? endExcl == null ? null : (endIncl = endExcl.minusDays(1)) : endIncl;
    }

    public LocalDate endExcl() {
        return endExcl;
    }

    /**
     * Checks if the date range encompasses the given date.
     *
     * @param date the date to check
     * @return true if the date is within this date range, false otherwise
     */
    public boolean encompasses(LocalDate date) {
        return !date.isBefore(startIncl()) && date.isBefore(endExcl());
    }

    /**
     * Checks if the date range fully encompasses the specified other date range.
     *
     * @param otherRange the date range to check against
     * @return true if this date range fully contains the other date range, false otherwise
     */
    public boolean encompasses(DateRange otherRange) {
        return !this.startIncl().isAfter(otherRange.startIncl()) &&
               !this.endExcl().isBefore(otherRange.endExcl());
    }

    public static IntStream endExclEpochDaysStream(LocalDate startIncl, LocalDate endExcl) {
        return IntStream.range((int) startIncl.toEpochDay(), (int) endExcl.toEpochDay());
    }

    public IntStream asEpochDaysStream() {
        return endExclEpochDaysStream(startIncl, endExcl);
    }

    /**
     * Subtracts the given {@code other} DateRange from this DateRange and returns the resulting non-overlapping part(s).
     * <p>
     * The result can be:
     * <ul>
     *     <li>Empty – if {@code other} fully covers this range.</li>
     *     <li>One DateRange – if {@code other} partially overlaps with the start or end.</li>
     *     <li>Two DateRanges – if {@code other} is strictly inside this range.</li>
     *     <li>Original DateRange – if there's no overlap at all.</li>
     * </ul>
     *
     * Examples:
     * <pre>
     * [2025-03-10, 2025-03-20) - [2025-03-12, 2025-03-18) = [2025-03-10, 2025-03-12), [2025-03-18, 2025-03-20)
     * [2025-03-10, 2025-03-20) - [2025-03-05, 2025-03-15) = [2025-03-15, 2025-03-20)
     * [2025-03-10, 2025-03-20) - [2025-03-15, 2025-03-25) = [2025-03-10, 2025-03-15)
     * [2025-03-10, 2025-03-20) - [2025-03-05, 2025-03-25) = (empty)
     * [2025-03-10, 2025-03-20) - [2025-03-20, 2025-03-25) = [2025-03-10, 2025-03-20)
     * </pre>
     *
     * @param other the {@code DateRange} to subtract from this range
     * @return a list of {@code DateRange} objects representing the difference
     */
    public List<DateRange> subtract(DateRange other) {
        if (other.startIncl().equals(other.endExcl())) {
            // Zero-length range, no subtraction needed
            return List.of(this);
        }

        List<DateRange> result = new ArrayList<>(2);

        // No overlap
        if (other.endExcl().isBefore(this.startIncl()) || other.startIncl().isAfter(this.endExcl())) {
            result.add(this);
            return result;
        }

        // Overlap at the beginning
        if (other.startIncl().isAfter(this.startIncl())) {
            result.add(new DateRange(this.startIncl(), other.startIncl()));
        }

        // Overlap at the end
        if (other.endExcl().isBefore(this.endExcl())) {
            result.add(new DateRange(other.endExcl(), this.endExcl()));
        }

        return result;
    }

    /**
     * Returns the smallest {@code DateRange} that fully covers both this range and the given {@code other} range.
     * <p>
     * If this range encompasses the other, then this range is returned.
     *
     * <p>Examples:</p>
     * <pre>
     * [2025-03-10, 2025-03-20) ∪ [2025-03-15, 2025-03-25) = [2025-03-10, 2025-03-25)
     * [2025-03-10, 2025-03-20) ∪ [2025-03-01, 2025-03-05) = [2025-03-01, 2025-03-20)
     * [2025-03-10, 2025-03-20) ∪ [2025-03-10, 2025-03-20) = [2025-03-10, 2025-03-20)
     * </pre>
     *
     * @param other the other {@code DateRange} to combine with this range
     * @return a {@code DateRange} representing the union of the two
     */
    public DateRange span(DateRange other) {
        return encompasses(other) ? this : new DateRange(
                minDate(startIncl, other.startIncl),
                maxDate(endExcl, other.endExcl)
        );
    }

    public DateRange span(LocalDate otherDateIncl) {
        return encompasses(otherDateIncl) ? this : new DateRange(
                minDate(startIncl, otherDateIncl),
                maxDate(endExcl, otherDateIncl.plusDays(1))
        );
    }

    public DateRange spanRight(LocalDate rightDateIncl) {
        return rightDateIncl.isBefore(endExcl) ? this : new DateRange(
                startIncl,
                maxDate(endExcl, rightDateIncl.plusDays(1))
        );
    }

    public DateRange spanLeft(LocalDate leftDateIncl) {
        return !leftDateIncl.isBefore(startIncl) ? this : new DateRange(
                minDate(startIncl, leftDateIncl),
                endExcl
        );
    }

    /**
     * Returns the intersection (overlapping portion) of this DateRange with the given {@code other} DateRange.
     * <p>
     * If there is no overlap, {@code null} is returned.
     * <p>
     * Examples:
     * <pre>
     * [2025-03-10, 2025-03-20) ∩ [2025-03-15, 2025-03-25) = [2025-03-15, 2025-03-20)
     * [2025-03-10, 2025-03-20) ∩ [2025-03-01, 2025-03-15) = [2025-03-10, 2025-03-15)
     * [2025-03-10, 2025-03-20) ∩ [2025-03-10, 2025-03-20) = [2025-03-10, 2025-03-20)
     * [2025-03-10, 2025-03-20) ∩ [2025-03-20, 2025-03-25) = null
     * </pre>
     *
     * @param other the other {@code DateRange} to intersect with
     * @return a {@code DateRange} representing the intersection, or {@code null} if there's no overlap
     */
    public DateRange intersect(DateRange other) {
        LocalDate otherStartIncl = other.startIncl();
        LocalDate otherEndExcl = other.endExcl();
        return intersect(otherStartIncl, otherEndExcl);
    }

    /**
     * See {@link DateRange#intersect(DateRange)}
     */
    public DateRange intersect(LocalDate otherStartIncl, LocalDate otherEndExcl) {
        LocalDate maxStartIncl = startIncl.isAfter(otherStartIncl) ? startIncl : otherStartIncl;
        LocalDate minEndExcl = endExcl.isBefore(otherEndExcl) ? endExcl : otherEndExcl;

        // If no actual overlap
        if (!maxStartIncl.isBefore(minEndExcl)) {
            return null;
        }

        return new DateRange(maxStartIncl, minEndExcl);
    }

    /**
     * Returns a new DateRange by extending this range toward the other disjoint range's nearest boundary.
     * <ul>
     *   <li>[1,4) extend [6,8) → [1,6): preserves this.startIncl, extends end to other.startIncl()</li>
     *   <li>[6,8) extend [1,4) → [4,8): preserves this.endExcl, extends start back to other.endExcl()</li>
     * </ul>
     * If the two ranges overlap or are adjacent, returns {@code this}.
     *
     * @param other the DateRange to extend toward (must be disjoint)
     * @return a new DateRange spanning the gap toward the other, or {@code this} if no disjoint gap
     */
    public DateRange extendTo(DateRange other) {
        if (this.endExcl().isBefore(other.startIncl())) {
            return new DateRange(this.startIncl(), other.startIncl());
        }
        if (other.endExcl().isBefore(this.startIncl())) {
            return new DateRange(other.endExcl(), this.endExcl());
        }
        return this;
    }

    /**
     * Returns a new DateRange that includes the specified inclusive date.
     * <p>
     * If the given date is before the current start date, the range is extended backwards.
     * If it is after the current inclusive end date, the range is extended forward.
     * If the date is already within the range, {@code this} is returned.
     *
     * @param dateIncl the date to include in the range (inclusive)
     * @return a new extended DateRange or {@code this} if the date is already included
     */
    public DateRange extendToIncl(LocalDate dateIncl) {
        if (dateIncl.isBefore(this.startIncl())) {
            return new DateRange(dateIncl, this.endExcl());
        }
        if (dateIncl.isAfter(this.endIncl())) {
            return new DateRange(this.startIncl(), dateIncl.plusDays(1));
        }
        return this;
    }

    /**
     * Returns a new DateRange that extends to (but does not include) the specified exclusive date.
     * <p>
     * If the given date is before the current start date, the range is extended backwards (by shifting start date).
     * If it is after the current exclusive end date, the range is extended forward.
     * If the date is already within or adjacent to the current range, {@code this} is returned.
     *
     * @param dateExcl the exclusive end date to extend the range to
     * @return a new extended DateRange or {@code this} if the date is already within or adjacent to the range
     */
    public DateRange extendToExcl(LocalDate dateExcl) {
        if (dateExcl.isBefore(this.startExcl())) {
            return new DateRange(dateExcl.plusDays(1), this.endExcl());
        }
        if (dateExcl.isAfter(this.endExcl())) {
            return new DateRange(this.startIncl(), dateExcl);
        }
        return this;
    }

    public DateRange shiftRight(int days) {
        return days == 0 ? this : new DateRange(this.startIncl().plusDays(days), this.endExcl().plusDays(days));
    }

    public DateRange shiftLeft(int days) {
        return shiftRight(-days);
    }

    public int length() {
        return (int) this.startIncl().until(this.endExcl(), ChronoUnit.DAYS);
    }

    @Override
    public String toString() {
        return "DateRange{[" + startIncl + "-" + endExcl + ")}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DateRange dateRange = (DateRange) obj;
        return Objects.equals(startIncl, dateRange.startIncl) && Objects.equals(endExcl, dateRange.endExcl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startIncl, endExcl);
    }
}
