package com.brinvex.java;

import java.time.LocalDate;
import java.util.Objects;

public final class DateRange {

    private LocalDate startExcl;
    private final LocalDate startIncl;
    private LocalDate endIncl;
    private final LocalDate endExcl;

    public static DateRange ofInclusive(LocalDate startIncl, LocalDate endIncl) {
        return new DateRange(startIncl, endIncl == null ? null : endIncl.plusDays(1));
    }

    public static DateRange ofStartExclusive(LocalDate startExcl, LocalDate endIncl) {
        return new DateRange(startExcl == null ? null : startExcl.plusDays(1), endIncl == null ? null : endIncl.plusDays(1));
    }

    public static DateRange ofEndExclusive(LocalDate startIncl, LocalDate endExcl) {
        return new DateRange(startIncl, endExcl);
    }

    public static DateRange ofExclusive(LocalDate startExcl, LocalDate endExcl) {
        return new DateRange(startExcl == null ? null : startExcl.plusDays(1), endExcl);
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

    public boolean contains(LocalDate date) {
        return !date.isBefore(startIncl) && date.isBefore(endExcl);
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
