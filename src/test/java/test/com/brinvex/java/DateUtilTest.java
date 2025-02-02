package test.com.brinvex.java;

import static com.brinvex.java.DateUtil.plusWorkDays;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class DateUtilTest {

    @Test
    void addZeroWorkDays() {
        LocalDate date = LocalDate.of(2025, 2, 3); // Monday
        // When adding 0 workdays, the result should be the same date.
        assertEquals(date, plusWorkDays(date, 0));
    }

    @Test
    void addWorkDaysPositiveSimple() {
        LocalDate date = LocalDate.of(2025, 2, 3); // Monday
        // Adding 3 workdays: Tuesday, Wednesday, Thursday.
        LocalDate expected = LocalDate.of(2025, 2, 6); // Thursday
        assertEquals(expected, plusWorkDays(date, 3));
    }

    @Test
    void addWorkDaysCrossWeekend() {
        LocalDate date = LocalDate.of(2025, 2, 6); // Thursday
        // Adding 3 workdays:
        // - Friday (Feb 7)
        // - Monday (Feb 10, since weekend is skipped)
        // - Tuesday (Feb 11)
        LocalDate expected = LocalDate.of(2025, 2, 11); // Tuesday
        assertEquals(expected, plusWorkDays(date, 3));
    }

    @Test
    void addWorkDaysFullWeek() {
        LocalDate date = LocalDate.of(2025, 2, 3); // Monday
        // Adding 5 workdays:
        // The result should be the next Monday because the weekend is skipped.
        // Monday (start) + 5 workdays = next Monday (Feb 10)
        LocalDate expected = LocalDate.of(2025, 2, 10);
        assertEquals(expected, plusWorkDays(date, 5));
    }

    @Test
    void subtractWorkDaysSimple() {
        LocalDate date = LocalDate.of(2025, 2, 10); // Monday
        // Subtracting 3 workdays:
        // Monday (Feb 10) -> previous Friday (Feb 7) counts as 1,
        // then Thursday (Feb 6) counts as 2,
        // then Wednesday (Feb 5) counts as 3.
        LocalDate expected = LocalDate.of(2025, 2, 5); // Wednesday
        assertEquals(expected, plusWorkDays(date, -3));
    }

    @Test
    void startOnWeekendForward() {
        // Start on a Saturday and add positive workdays.
        LocalDate saturday = LocalDate.of(2025, 2, 8); // Saturday
        // The method normalizes Saturday to the following Monday (Feb 10).
        // Then, adding 2 workdays: Tuesday (Feb 11) and Wednesday (Feb 12).
        LocalDate expected = LocalDate.of(2025, 2, 12); // Wednesday
        assertEquals(expected, plusWorkDays(saturday, 2));
    }

    @Test
    void startOnWeekendBackward() {
        // Start on a Sunday and subtract workdays.
        LocalDate sunday = LocalDate.of(2025, 2, 9); // Sunday
        // The method normalizes Sunday backward to the preceding Friday (Feb 7).
        // Then subtracting 2 workdays: Thursday (Feb 6) and Wednesday (Feb 5).
        LocalDate expected = LocalDate.of(2025, 2, 5); // Wednesday
        assertEquals(expected, plusWorkDays(sunday, -2));
    }
}
