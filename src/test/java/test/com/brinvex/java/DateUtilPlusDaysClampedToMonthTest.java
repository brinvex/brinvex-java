package test.com.brinvex.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import com.brinvex.java.DateUtil;
import org.junit.jupiter.api.Test;

public class DateUtilPlusDaysClampedToMonthTest {

    @Test
    public void testAddWithinMonth() {
        LocalDate date = LocalDate.of(2025, 7, 10);
        LocalDate result = DateUtil.plusDaysClampedToMonth(date, 5);
        assertEquals(LocalDate.of(2025, 7, 15), result);
    }

    @Test
    public void testAddCrossEndOfMonth() {
        LocalDate date = LocalDate.of(2025, 7, 28);
        LocalDate result = DateUtil.plusDaysClampedToMonth(date, 5);
        assertEquals(LocalDate.of(2025, 7, 31), result);
    }

    @Test
    public void testSubtractWithinMonth() {
        LocalDate date = LocalDate.of(2025, 7, 10);
        LocalDate result = DateUtil.plusDaysClampedToMonth(date, -5);
        assertEquals(LocalDate.of(2025, 7, 5), result);
    }

    @Test
    public void testSubtractPastStartOfMonth() {
        LocalDate date = LocalDate.of(2025, 7, 3);
        LocalDate result = DateUtil.plusDaysClampedToMonth(date, -5);
        assertEquals(LocalDate.of(2025, 7, 1), result);
    }

    @Test
    public void testAddZeroDays() {
        LocalDate date = LocalDate.of(2025, 7, 15);
        LocalDate result = DateUtil.plusDaysClampedToMonth(date, 0);
        assertEquals(date, result);
    }
}
