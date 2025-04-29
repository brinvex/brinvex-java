package test.com.brinvex.java;

import com.brinvex.java.DateRange;
import org.junit.jupiter.api.Test;

import static com.brinvex.java.DateRange.dateRangeIncl;
import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.*;

public class DateRangeExtendTests {

    @Test
    void testForwardExtension() {
        DateRange a = dateRangeIncl(parse("2025-01-01"), parse("2025-01-03")); // [2025-01-01,2025-01-04)
        DateRange b = dateRangeIncl(parse("2025-01-06"), parse("2025-01-07")); // [2025-01-06,2025-01-08)

        DateRange extended = a.extendTo(b);
        assertNotNull(extended);
        assertEquals(parse("2025-01-01"), extended.startIncl());
        assertEquals(parse("2025-01-06"), extended.endExcl());
    }

    @Test
    void testBackwardExtension() {
        DateRange a = dateRangeIncl(parse("2025-06-06"), parse("2025-06-07")); // [2025-06-06,2025-06-08)
        DateRange b = dateRangeIncl(parse("2025-06-01"), parse("2025-06-03")); // [2025-06-01,2025-06-04)

        DateRange extended = a.extendTo(b);
        assertNotNull(extended);
        assertEquals(parse("2025-06-04"), extended.startIncl());
        assertEquals(parse("2025-06-08"), extended.endExcl());
    }

    @Test
    void testOverlappingRanges() {
        DateRange a = dateRangeIncl(parse("2025-03-10"), parse("2025-03-15")); // [2025-03-10,2025-03-16)
        DateRange b = dateRangeIncl(parse("2025-03-14"), parse("2025-03-20")); // overlap

        assertNull(a.extendTo(b));
        assertNull(b.extendTo(a));
    }

    @Test
    void testAdjacentRanges() {
        DateRange a = dateRangeIncl(parse("2025-04-01"), parse("2025-04-03")); // [2025-04-01,2025-04-04)
        DateRange b = dateRangeIncl(parse("2025-04-04"), parse("2025-04-05")); // adjacent at 4

        assertNull(a.extendTo(b));
        assertNull(b.extendTo(a));
    }

    @Test
    void testZeroLengthGap() {
        // Completely disjoint but separated by more than one day
        DateRange a = dateRangeIncl(parse("2025-07-01"), parse("2025-07-02")); // [2025-07-01,2025-07-03)
        DateRange b = dateRangeIncl(parse("2025-07-04"), parse("2025-07-04")); // [2025-07-04,2025-07-05)

        DateRange extended = a.extendTo(b);
        assertNotNull(extended);
        assertEquals(parse("2025-07-01"), extended.startIncl());
        assertEquals(parse("2025-07-04"), extended.endExcl());
    }
}
