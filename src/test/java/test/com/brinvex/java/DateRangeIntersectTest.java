package test.com.brinvex.java;

import com.brinvex.java.DateRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.brinvex.java.DateRange.dateRangeEndExcl;

public class DateRangeIntersectTest {

    @Test
    void intersect_overlappingAtEndOfThis_returnsOverlap() {
        DateRange base = dateRangeEndExcl("2025-03-10", "2025-03-20");
        DateRange other = dateRangeEndExcl("2025-03-15", "2025-03-25");

        DateRange result = base.intersect(other);
        assertEquals(dateRangeEndExcl("2025-03-15", "2025-03-20"), result);
    }

    @Test
    void intersect_overlappingAtStartOfThis_returnsOverlap() {
        DateRange base = dateRangeEndExcl("2025-03-10", "2025-03-20");
        DateRange other = dateRangeEndExcl("2025-03-01", "2025-03-15");

        DateRange result = base.intersect(other);
        assertEquals(dateRangeEndExcl("2025-03-10", "2025-03-15"), result);
    }

    @Test
    void intersect_fullyContainsOther_returnsOther() {
        DateRange base = dateRangeEndExcl("2025-03-10", "2025-03-20");
        DateRange other = dateRangeEndExcl("2025-03-12", "2025-03-18");

        DateRange result = base.intersect(other);
        assertEquals(other, result);
    }

    @Test
    void intersect_otherFullyContainsBase_returnsBase() {
        DateRange base = dateRangeEndExcl("2025-03-10", "2025-03-20");
        DateRange other = dateRangeEndExcl("2025-03-05", "2025-03-25");

        DateRange result = base.intersect(other);
        assertEquals(base, result);
    }

    @Test
    void intersect_exactMatch_returnsSameRange() {
        DateRange base = dateRangeEndExcl("2025-03-10", "2025-03-20");
        DateRange other = dateRangeEndExcl("2025-03-10", "2025-03-20");

        DateRange result = base.intersect(other);
        assertEquals(base, result);
    }

    @Test
    void intersect_adjacentBefore_returnsNull() {
        DateRange base = dateRangeEndExcl("2025-03-10", "2025-03-20");
        DateRange other = dateRangeEndExcl("2025-03-01", "2025-03-10");

        assertNull(base.intersect(other));
    }

    @Test
    void intersect_adjacentAfter_returnsNull() {
        DateRange base = dateRangeEndExcl("2025-03-10", "2025-03-20");
        DateRange other = dateRangeEndExcl("2025-03-20", "2025-03-25");

        assertNull(base.intersect(other));
    }

    @Test
    void intersect_noOverlapBefore_returnsNull() {
        DateRange base = dateRangeEndExcl("2025-03-10", "2025-03-20");
        DateRange other = dateRangeEndExcl("2025-02-01", "2025-02-28");

        assertNull(base.intersect(other));
    }

    @Test
    void intersect_noOverlapAfter_returnsNull() {
        DateRange base = dateRangeEndExcl("2025-03-10", "2025-03-20");
        DateRange other = dateRangeEndExcl("2025-04-01", "2025-04-10");

        assertNull(base.intersect(other));
    }

    @Test
    void intersect_zeroLengthOther_returnsNull() {
        DateRange base = dateRangeEndExcl("2025-03-10", "2025-03-20");
        DateRange other = dateRangeEndExcl("2025-03-15", "2025-03-15"); // empty

        assertNull(base.intersect(other));
    }

    @Test
    void intersect_zeroLengthBase_returnsNullEvenIfOtherCovers() {
        DateRange base = dateRangeEndExcl("2025-03-15", "2025-03-15"); // empty
        DateRange other = dateRangeEndExcl("2025-03-10", "2025-03-20");

        assertNull(base.intersect(other));
    }
}
