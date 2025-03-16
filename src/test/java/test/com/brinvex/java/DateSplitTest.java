package test.com.brinvex.java;

import com.brinvex.java.DateInterval;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.brinvex.java.DateUtil.splitIntoYearlyIntervals;
import static org.junit.jupiter.api.Assertions.*;

class DateSplitTest {

    @Test
    void testIntervalsWithEndIncluded() {
        LocalDate fromDateIncl = LocalDate.of(2020, 3, 15);
        LocalDate toDateIncl = LocalDate.of(2023, 8, 10);
        
        List<DateInterval> intervals = splitIntoYearlyIntervals(fromDateIncl, toDateIncl);
        
        assertEquals(4, intervals.size());
        assertEquals(LocalDate.of(2020, 3, 15), intervals.get(0).start());
        assertEquals(LocalDate.of(2020, 12, 31), intervals.get(0).end());
        assertEquals(LocalDate.of(2023, 1, 1), intervals.get(3).start());
        assertEquals(LocalDate.of(2023, 8, 10), intervals.get(3).end());
    }

    @Test
    void testSingleIntervalExactYear() {
        LocalDate fromDateIncl = LocalDate.of(2021, 6, 1);
        LocalDate toDateIncl = LocalDate.of(2021, 12, 31);
        
        List<DateInterval> intervals = splitIntoYearlyIntervals(fromDateIncl, toDateIncl);
        
        assertEquals(1, intervals.size());
        assertEquals(LocalDate.of(2021, 6, 1), intervals.get(0).start());
        assertEquals(LocalDate.of(2021, 12, 31), intervals.get(0).end());
    }

    @Test
    void testSingleIntervalNoFullYear() {
        LocalDate fromDateIncl = LocalDate.of(2022, 11, 1);
        LocalDate toDateIncl = LocalDate.of(2023, 1, 15);
        
        List<DateInterval> intervals = splitIntoYearlyIntervals(fromDateIncl, toDateIncl);
        
        assertEquals(2, intervals.size());
        assertEquals(LocalDate.of(2022, 11, 1), intervals.get(0).start());
        assertEquals(LocalDate.of(2022, 12, 31), intervals.get(0).end());
        assertEquals(LocalDate.of(2023, 1, 1), intervals.get(1).start());
        assertEquals(LocalDate.of(2023, 1, 15), intervals.get(1).end());
    }

    @Test
    void testEdgeCaseWithSameStartAndEnd() {
        LocalDate fromDateIncl = LocalDate.of(2021, 1, 1);
        LocalDate toDateIncl = LocalDate.of(2021, 1, 1);
        
        List<DateInterval> intervals = splitIntoYearlyIntervals(fromDateIncl, toDateIncl);
        
        assertEquals(1, intervals.size());
        assertEquals(LocalDate.of(2021, 1, 1), intervals.get(0).start());
        assertEquals(LocalDate.of(2021, 1, 1), intervals.get(0).end());
    }

    @Test
    void testEmptyInterval() {
        LocalDate fromDateIncl = LocalDate.of(2022, 1, 1);
        LocalDate toDateIncl = LocalDate.of(2021, 1, 1);
        
        List<DateInterval> intervals = splitIntoYearlyIntervals(fromDateIncl, toDateIncl);
        
        assertTrue(intervals.isEmpty());
    }
}
