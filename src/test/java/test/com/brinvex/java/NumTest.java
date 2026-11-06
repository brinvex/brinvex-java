package test.com.brinvex.java;

import com.brinvex.java.Num;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NumTest {

    @Test
    void setScaleShortcuts() {
        BigDecimal bd = new BigDecimal("1.23456789");
        assertEquals(new BigDecimal("1"), Num.setScale0(bd));
        assertEquals(new BigDecimal("1.2"), Num.setScale1(bd));
        assertEquals(new BigDecimal("1.23"), Num.setScale2(bd));
        assertEquals(new BigDecimal("1.235"), Num.setScale3(bd));
        assertEquals(new BigDecimal("1.2346"), Num.setScale4(bd));
        assertEquals(new BigDecimal("1.234568"), Num.setScale6(bd));
        assertEquals(new BigDecimal("1.23456789"), Num.setScale8(bd));
    }

    @Test
    void setScaleShortcutsNull() {
        assertNull(Num.setScale1(null));
    }
}
