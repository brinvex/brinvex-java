package com.brinvex.java;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {

    public static LocalDate firstDayOfYear(int year) {
        return LocalDate.of(year, Month.JANUARY, 1);
    }

    public static LocalDate lastDayOfYear(int year) {
        return LocalDate.of(year, Month.DECEMBER, 31);
    }

    public static LocalDate maxDate(LocalDate date1, LocalDate date2) {
        return date1.isAfter(date2) ? date1 : date2;
    }

    public static LocalDateTime maxDateTime(LocalDateTime date1, LocalDateTime date2) {
        return date1.isAfter(date2) ? date1 : date2;
    }

    public static LocalDate minDate(LocalDate date1, LocalDate date2) {
        return date1.isBefore(date2) ? date1 : date2;
    }

    public static LocalDate minDate(LocalDate date1, LocalDate date2, LocalDate date3) {
        return minDate(date1, minDate(date2, date3));
    }

    public static LocalDateTime minDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isBefore(dateTime2) ? dateTime1 : dateTime2;
    }

    public static boolean bothNullOrEqual(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 != null && dateTime2 != null) {
            return dateTime1.isEqual(dateTime2);
        }
        return dateTime1 == null && dateTime2 == null;
    }

    public static boolean bothNullOrEqual(LocalDate date1, LocalDate date2) {
        if (date1 != null && date2 != null) {
            return date1.isEqual(date2);
        }
        return date1 == null && date2 == null;
    }

    public static LocalDate adjustWeekendToFriday(LocalDate day) {
        return day == null ? null : switch (day.getDayOfWeek()) {
            case SATURDAY -> day.minusDays(1);
            case SUNDAY -> day.minusDays(2);
            default -> day;
        };
    }

    public static LocalDate adjustToFirstDayOfPreviousYear(LocalDate day) {
        return day == null ? null : day.withDayOfYear(1).minusYears(1);
    }

    public static boolean isWeekend(LocalDate day) {
        return switch (day.getDayOfWeek()) {
            case SATURDAY, SUNDAY -> true;
            default -> false;
        };
    }

    /**
     * Adds (or subtracts) the specified number of work days (Monday through Friday) to/from the given date.
     * If the given date falls on a weekend, it is normalized to the nearest workday:
     * <ul>
     *     <li>For a positive daysToAdd: Saturday and Sunday are moved to the following Monday.</li>
     *     <li>For a negative daysToAdd: Saturday and Sunday are moved to the preceding Friday.</li>
     * </ul>
     *
     * @param date      the starting date
     * @param daysToAdd the number of work days to add (or subtract, if negative)
     * @return the resulting LocalDate after adding the work days
     */
    @SuppressWarnings("ExtractMethodRecommender")
    public static LocalDate plusWorkDays(LocalDate date, int daysToAdd) {
        if (daysToAdd == 0) {
            return date;
        }

        // Normalize the start date if it falls on a weekend.
        DayOfWeek dow = date.getDayOfWeek();
        if (daysToAdd > 0) {
            if (dow == DayOfWeek.SATURDAY) {
                date = date.plusDays(2);
            } else if (dow == DayOfWeek.SUNDAY) {
                date = date.plusDays(1);
            }
        } else {
            if (dow == DayOfWeek.SATURDAY) {
                date = date.minusDays(1);
            } else if (dow == DayOfWeek.SUNDAY) {
                date = date.minusDays(2);
            }
        }

        if (daysToAdd > 0) {
            // Calculate whole weeks and extra days.
            int fullWeeks = daysToAdd / 5;
            int extraDays = daysToAdd % 5;
            LocalDate result = date.plusDays(fullWeeks * 7L);

            // Determine day-of-week as an int where Monday = 1 ... Friday = 5.
            int currentDow = result.getDayOfWeek().getValue();
            // If adding the extra days would run past Friday, skip the weekend.
            if (currentDow + extraDays > 5) {
                extraDays += 2;
            }
            result = result.plusDays(extraDays);
            return result;
        } else { // daysToAdd < 0
            // Convert daysToAdd to a positive number for calculation.
            int absDays = -daysToAdd;
            int fullWeeks = absDays / 5;
            int extraDays = absDays % 5;
            LocalDate result = date.minusDays(fullWeeks * 7L);

            int currentDow = result.getDayOfWeek().getValue();
            // If subtracting extra days would run before Monday, skip the weekend.
            if (currentDow - extraDays < 1) {
                extraDays += 2;
            }
            result = result.minusDays(extraDays);
            return result;
        }
    }

    public static boolean isFirstDayOfMonth(LocalDate day) {
        return day.getDayOfMonth() == 1;
    }

    public static boolean isLastDayOfMonth(LocalDate day) {
        return day.plusDays(1).getDayOfMonth() == 1;
    }

    public static boolean isFirstDayOfYear(LocalDate day) {
        return day.getDayOfYear() == 1;
    }

    public static List<DateRange> splitIntoYearlyIntervals(DateRange range) {
        LocalDate startIncl = range.startIncl();
        LocalDate endIncl = range.endIncl();

        if (startIncl == null || endIncl == null) {
            throw new IllegalArgumentException("Invalid date range: %s".formatted(range));
        }

        List<DateRange> results = new ArrayList<>();
        if (startIncl.isAfter(endIncl)) {
            return results;
        }

        // First interval (preserve type)
        LocalDate endOfFirstYear = LocalDate.of(startIncl.getYear(), 12, 31);
        if (endOfFirstYear.isAfter(endIncl)) {
            results.add(range);
            return results;
        }
        results.add(createDateRange(range, startIncl, endOfFirstYear));
        startIncl = endOfFirstYear.plusDays(1);

        // Middle intervals (fully inclusive)
        while (startIncl.plusYears(1).minusDays(1).isBefore(endIncl)) {
            LocalDate endOfYear = startIncl.plusYears(1).minusDays(1);
            results.add(new DateRange.InclDateRange(startIncl, endOfYear));
            startIncl = endOfYear.plusDays(1);
        }

        // Last interval (preserve type)
        if (startIncl.isBefore(endIncl) || startIncl.equals(endIncl)) {
            results.add(createDateRange(range, startIncl, endIncl));
        }

        return results;
    }

    private static DateRange createDateRange(DateRange original, LocalDate startIncl, LocalDate endIncl) {
        if (original instanceof DateRange.InclDateRange) {
            return new DateRange.InclDateRange(startIncl, endIncl);
        } else if (original instanceof DateRange.StartExclDateRange) {
            return new DateRange.StartExclDateRange(startIncl.minusDays(1), endIncl);
        } else if (original instanceof DateRange.EndExclDateRange) {
            return new DateRange.EndExclDateRange(startIncl, endIncl.plusDays(1));
        } else if (original instanceof DateRange.ExclDateRange) {
            return new DateRange.ExclDateRange(startIncl.minusDays(1), endIncl.plusDays(1));
        }
        throw new IllegalArgumentException("Unknown DateRange type: " + original);
    }}
