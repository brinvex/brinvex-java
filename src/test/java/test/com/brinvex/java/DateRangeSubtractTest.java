package test.com.brinvex.java;

import com.brinvex.java.DateRange;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DateRangeSubtractTest {

    private static DateRange dr(String start, String end) {
        return new DateRange(LocalDate.parse(start), LocalDate.parse(end));
    }

    @Test
    void subtract_whenOtherIsStrictlyWithin_returnsTwoRanges() {
        DateRange base = dr("2025-03-10", "2025-03-20");
        DateRange other = dr("2025-03-12", "2025-03-18");

        List<DateRange> result = base.subtract(other);

        assertEquals(2, result.size());
        assertEquals(dr("2025-03-10", "2025-03-12"), result.get(0));
        assertEquals(dr("2025-03-18", "2025-03-20"), result.get(1));
    }

    @Test
    void subtract_whenOtherOverlapsStart_returnsTailRange() {
        DateRange base = dr("2025-03-10", "2025-03-20");
        DateRange other = dr("2025-03-05", "2025-03-15");

        List<DateRange> result = base.subtract(other);

        assertEquals(1, result.size());
        assertEquals(dr("2025-03-15", "2025-03-20"), result.get(0));
    }

    @Test
    void subtract_whenOtherOverlapsEnd_returnsHeadRange() {
        DateRange base = dr("2025-03-10", "2025-03-20");
        DateRange other = dr("2025-03-15", "2025-03-25");

        List<DateRange> result = base.subtract(other);

        assertEquals(1, result.size());
        assertEquals(dr("2025-03-10", "2025-03-15"), result.get(0));
    }

    @Test
    void subtract_whenOtherFullyCoversBase_returnsEmpty() {
        DateRange base = dr("2025-03-10", "2025-03-20");
        DateRange other = dr("2025-03-05", "2025-03-25");

        List<DateRange> result = base.subtract(other);

        assertTrue(result.isEmpty());
    }

    @Test
    void subtract_whenOtherIsCompletelyBefore_returnsOriginal() {
        DateRange base = dr("2025-03-10", "2025-03-20");
        DateRange other = dr("2025-03-01", "2025-03-05");

        List<DateRange> result = base.subtract(other);

        assertEquals(1, result.size());
        assertEquals(base, result.get(0));
    }

    @Test
    void subtract_whenOtherIsCompletelyAfter_returnsOriginal() {
        DateRange base = dr("2025-03-10", "2025-03-20");
        DateRange other = dr("2025-03-20", "2025-03-25");

        List<DateRange> result = base.subtract(other);

        assertEquals(1, result.size());
        assertEquals(base, result.get(0));
    }

    @Test
    void subtract_whenExactMatch_returnsEmpty() {
        DateRange base = dr("2025-03-10", "2025-03-20");
        DateRange other = dr("2025-03-10", "2025-03-20");

        List<DateRange> result = base.subtract(other);

        assertTrue(result.isEmpty());
    }

    @Test
    void subtract_whenZeroLengthRangeInside_returnsOriginal() {
        DateRange base = dr("2025-03-10", "2025-03-20");
        DateRange other = dr("2025-03-15", "2025-03-15"); // Zero-length

        List<DateRange> result = base.subtract(other);

        assertEquals(1, result.size());
        assertEquals(base, result.get(0));
    }
}
