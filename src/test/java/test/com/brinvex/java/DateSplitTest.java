package test.com.brinvex.java;

import com.brinvex.java.DateRange;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.brinvex.java.DateUtil.splitIntoYearlyIntervals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DateSplitTest {

    @Test
    void testSplitFullyInclusiveRange() {
        LocalDate start = LocalDate.parse("2022-03-15");
        LocalDate end = LocalDate.parse("2024-06-10");
        DateRange range = DateRange.dateRangeIncl(start, end);
        List<DateRange> result = splitIntoYearlyIntervals(range);

        assertEquals(3, result.size());
        assertEquals(LocalDate.parse("2022-03-15"), result.get(0).startIncl());
        assertEquals(LocalDate.parse("2022-12-31"), result.get(0).endIncl());
        assertEquals(LocalDate.parse("2023-01-01"), result.get(1).startIncl());
        assertEquals(LocalDate.parse("2023-12-31"), result.get(1).endIncl());
        assertEquals(LocalDate.parse("2024-01-01"), result.get(2).startIncl());
        assertEquals(LocalDate.parse("2024-06-10"), result.get(2).endIncl());
    }

    @Test
    void testSplitStartExclusiveRange() {
        LocalDate start = LocalDate.parse("2021-06-05");
        LocalDate end = LocalDate.parse("2023-08-20");
        DateRange range = DateRange.dateRangeStartExcl(start.minusDays(1), end);
        List<DateRange> result = splitIntoYearlyIntervals(range);

        assertEquals(3, result.size());
    }

    @Test
    void testSplitEndExclusiveRange() {
        LocalDate start = LocalDate.parse("2019-11-10");
        LocalDate end = LocalDate.parse("2022-02-25");
        DateRange range = DateRange.dateRangeEndExcl(start, end.plusDays(1));
        List<DateRange> result = splitIntoYearlyIntervals(range);

        assertEquals(4, result.size());
    }

    @Test
    void testSplitExclRange() {
        LocalDate start = LocalDate.parse("2017-07-07");
        LocalDate end = LocalDate.parse("2019-09-18");
        DateRange range = DateRange.dateRangeEndExcl(start.minusDays(1), end.plusDays(1));
        List<DateRange> result = splitIntoYearlyIntervals(range);

        assertEquals(3, result.size());
    }

    @Test
    void testSingleYearRange() {
        LocalDate start = LocalDate.parse("2025-02-03");
        LocalDate end = LocalDate.parse("2025-11-20");
        DateRange range = DateRange.dateRangeIncl(start, end);
        List<DateRange> result = splitIntoYearlyIntervals(range);

        assertEquals(1, result.size());
    }

    @Test
    void testExactYearRange() {
        LocalDate start = LocalDate.parse("2020-01-01");
        LocalDate end = LocalDate.parse("2020-12-31");
        DateRange range = DateRange.dateRangeIncl(start, end);
        List<DateRange> result = splitIntoYearlyIntervals(range);

        assertEquals(1, result.size());
    }

    @Test
    void testOneDayRange() {
        LocalDate start = LocalDate.parse("2023-05-05");
        LocalDate end = LocalDate.parse("2023-05-05");
        DateRange range = DateRange.dateRangeIncl(start, end);
        List<DateRange> result = splitIntoYearlyIntervals(range);

        assertEquals(1, result.size());
    }

    @Test
    void testOneDayRangeExcl() {
        LocalDate start = LocalDate.parse("2023-05-05");
        LocalDate end = LocalDate.parse("2023-05-06");
        DateRange range = DateRange.dateRangeEndExcl(start, end);
        List<DateRange> result = splitIntoYearlyIntervals(range);

        assertEquals(1, result.size());
    }

    @Test
    void testLeapYearRange() {
        LocalDate start = LocalDate.parse("2024-02-29");
        LocalDate end = LocalDate.parse("2025-03-01");
        DateRange range = DateRange.dateRangeIncl(start, end);
        List<DateRange> result = splitIntoYearlyIntervals(range);

        assertEquals(2, result.size());
    }
}
