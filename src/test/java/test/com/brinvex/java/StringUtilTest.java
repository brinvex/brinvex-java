package test.com.brinvex.java;

import com.brinvex.java.StringUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StringUtilTest {

    @Test
    public void generateWords() {
        assertEquals(
                List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"),
                StringUtil.generateWords(StringUtil.Constants.UP_LETTER_CHARS));

        assertEquals(
                List.of("A1", "A2", "B1", "B2", "C1", "C2"),
                StringUtil.generateWords("ABC".toCharArray(), "12".toCharArray()));

        assertEquals(
                List.of("AX1", "AX2", "BX1", "BX2", "CX1", "CX2"),
                StringUtil.generateWords("ABC".toCharArray(), "X".toCharArray(), "12".toCharArray()));
    }

    @Test
    public void splitByPercent1() {
        String s1 = "every month you send the agency a single payment that is portioned out to each of your creditors until";
        String s2 = " your debts are paid off";
        String s = s1 + s2;
        String[] parts = StringUtil.splitByPercentile(s, 80);

        assertNotNull(parts);
        assertEquals(s1, parts[0]);
        assertEquals(s2, parts[1]);
    }

    @Test
    public void splitByPercent2() {
        String s1 = "every   month \t you send the agency a single payment that is portioned out to each of your creditors until";
        String s2 = " your debts are paid off";
        String s = s1 + s2;
        String[] parts = StringUtil.splitByPercentile(s, 80);

        assertNotNull(parts);
        assertEquals(s1, parts[0]);
        assertEquals(s2, parts[1]);
    }

    @EnabledForJreRange(min = JRE.JAVA_21)
    @Test
    public void removeEmojis() {
        String orig = "a✅b⭐c❌d123";
        String clean = StringUtil.deleteEmojiPresentations(orig);
        assertEquals("abcd123", clean);
    }
}
