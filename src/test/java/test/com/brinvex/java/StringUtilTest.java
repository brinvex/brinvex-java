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

    @Test
    public void removeEmojis() {
        String orig = "a✅b⭐c❌d123";
        String clean = StringUtil.deleteEmojiPresentations(orig);
        assertEquals("abcd123", clean);
    }

    @Test
    void testNormalSpace() {
        String input = "123 456"; // U+0020 normal space
        assertEquals("123456", StringUtil.deleteAllWhitespaces(input));
        assertEquals("123456", StringUtil.deleteAllWhitespacesAndSpaceChars(input));
    }

    @Test
    void testNoBreakSpace() {
        String input = "123\u00A0456"; // U+00A0 NO-BREAK SPACE
        // isWhitespace = false → method 1 does not remove it
        assertEquals("123\u00A0456", StringUtil.deleteAllWhitespaces(input));
        // isSpaceChar = true → method 2 removes it
        assertEquals("123456", StringUtil.deleteAllWhitespacesAndSpaceChars(input));
    }

    @Test
    void testTab() {
        String input = "123\t456"; // U+0009 TAB
        // isWhitespace = true → method 1 removes it
        assertEquals("123456", StringUtil.deleteAllWhitespaces(input));
        // isSpaceChar = false → method 2 also removes because it also checks isWhitespace
        assertEquals("123456", StringUtil.deleteAllWhitespacesAndSpaceChars(input));
    }

    @Test
    void testMixedSpaces() {
        String input = "1 2\u00A03\t4";
        // method 1 removes normal space + tab, but not NBSP
        assertEquals("12\u00A034", StringUtil.deleteAllWhitespaces(input));
        // method 2 removes all of them
        assertEquals("1234", StringUtil.deleteAllWhitespacesAndSpaceChars(input));
    }
}
