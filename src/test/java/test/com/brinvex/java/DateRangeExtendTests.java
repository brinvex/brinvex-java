package test.com.brinvex.java;

import com.brinvex.java.DateRange;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.brinvex.java.DateRange.dateRangeIncl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class DateRangeExtendTests {

    @Test
    void testForwardExtension() {
        DateRange a = dateRangeIncl(LocalDate.parse("2025-01-01"), LocalDate.parse("2025-01-03")); // [2025-01-01,2025-01-04)
        DateRange b = dateRangeIncl(LocalDate.parse("2025-01-06"), LocalDate.parse("2025-01-07")); // [2025-01-06,2025-01-08)

        DateRange extended = a.extendTo(b);
        assertNotNull(extended);
        assertEquals(LocalDate.parse("2025-01-01"), extended.startIncl());
        assertEquals(LocalDate.parse("2025-01-06"), extended.endExcl());
    }

    @Test
    void testBackwardExtension() {
        DateRange a = dateRangeIncl(LocalDate.parse("2025-06-06"), LocalDate.parse("2025-06-07")); // [2025-06-06,2025-06-08)
        DateRange b = dateRangeIncl(LocalDate.parse("2025-06-01"), LocalDate.parse("2025-06-03")); // [2025-06-01,2025-06-04)

        DateRange extended = a.extendTo(b);
        assertNotNull(extended);
        assertEquals(LocalDate.parse("2025-06-04"), extended.startIncl());
        assertEquals(LocalDate.parse("2025-06-08"), extended.endExcl());
    }

    @Test
    void testOverlappingRanges() {
        DateRange a = dateRangeIncl(LocalDate.parse("2025-03-10"), LocalDate.parse("2025-03-15")); // [2025-03-10,2025-03-16)
        DateRange b = dateRangeIncl(LocalDate.parse("2025-03-14"), LocalDate.parse("2025-03-20")); // overlap

        assertSame(a, a.extendTo(b));
        assertSame(b, b.extendTo(a));
    }

    @Test
    void testAdjacentRanges() {
        DateRange a = dateRangeIncl(LocalDate.parse("2025-04-01"), LocalDate.parse("2025-04-03")); // [2025-04-01,2025-04-04)
        DateRange b = dateRangeIncl(LocalDate.parse("2025-04-04"), LocalDate.parse("2025-04-05")); // adjacent at 4

        assertSame(a, a.extendTo(b));
        assertSame(b, b.extendTo(a));
    }

    @Test
    void testZeroLengthGap() {
        // Completely disjoint but separated by more than one day
        DateRange a = dateRangeIncl(LocalDate.parse("2025-07-01"), LocalDate.parse("2025-07-02")); // [2025-07-01,2025-07-03)
        DateRange b = dateRangeIncl(LocalDate.parse("2025-07-04"), LocalDate.parse("2025-07-04")); // [2025-07-04,2025-07-05)

        DateRange extended = a.extendTo(b);
        assertNotNull(extended);
        assertEquals(LocalDate.parse("2025-07-01"), extended.startIncl());
        assertEquals(LocalDate.parse("2025-07-04"), extended.endExcl());
    }

    @Test
    void testExtendToInclBefore() {
        DateRange a = dateRangeIncl(LocalDate.parse("2025-05-05"), LocalDate.parse("2025-05-10"));
        DateRange extended = a.extendToIncl(LocalDate.parse("2025-05-01"));
        assertNotNull(extended);
        assertEquals(LocalDate.parse("2025-05-01"), extended.startIncl());
        assertEquals(a.endExcl(), extended.endExcl());
    }

    @Test
    void testExtendToInclAfter() {
        DateRange a = dateRangeIncl(LocalDate.parse("2025-05-05"), LocalDate.parse("2025-05-10"));
        DateRange extended = a.extendToIncl(LocalDate.parse("2025-05-15"));
        assertNotNull(extended);
        assertEquals(a.startIncl(), extended.startIncl());
        assertEquals(LocalDate.parse("2025-05-16"), extended.endExcl());
    }

    @Test
    void testExtendToInclInside() {
        DateRange a = dateRangeIncl(LocalDate.parse("2025-05-05"), LocalDate.parse("2025-05-10"));
        assertSame(a, a.extendToIncl(LocalDate.parse("2025-05-07")));
    }

    @Test
    void testExtendToExclBefore() {
        DateRange a = dateRangeIncl(LocalDate.parse("2025-05-05"), LocalDate.parse("2025-05-10"));
        DateRange extended = a.extendToExcl(LocalDate.parse("2025-05-03"));
        assertNotNull(extended);
        assertEquals(LocalDate.parse("2025-05-04"), extended.startIncl());
        assertEquals(a.endExcl(), extended.endExcl());
    }

    @Test
    void testExtendToExclAfter() {
        DateRange a = dateRangeIncl(LocalDate.parse("2025-05-05"), LocalDate.parse("2025-05-10"));
        DateRange extended = a.extendToExcl(LocalDate.parse("2025-05-15"));
        assertNotNull(extended);
        assertEquals(a.startIncl(), extended.startIncl());
        assertEquals(LocalDate.parse("2025-05-15"), extended.endExcl());
    }

    @Test
    void testExtendToExclInside() {
        DateRange a = dateRangeIncl(LocalDate.parse("2025-05-05"), LocalDate.parse("2025-05-10"));
        assertSame(a, a.extendToExcl(LocalDate.parse("2025-05-08")));
    }
}