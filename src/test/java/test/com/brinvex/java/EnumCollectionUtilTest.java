package test.com.brinvex.java;

import com.brinvex.java.collection.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class EnumCollectionUtilTest {

    private enum SampleEnum {
        FIRST, SECOND, THIRD
    }

    @Test
    void testEnumMapWithOnePair() {
        EnumMap<SampleEnum, String> map = CollectionUtil.enumMap(SampleEnum.FIRST, "Value1");

        assertNotNull(map, "Map should not be null");
        assertEquals(1, map.size(), "Map should contain exactly one entry");
        assertEquals("Value1", map.get(SampleEnum.FIRST), "Value should match the provided input");
    }

    @Test
    void testEnumMapWithTwoPairs() {
        EnumMap<SampleEnum, String> map = CollectionUtil.enumMap(SampleEnum.FIRST, "Value1", SampleEnum.SECOND, "Value2");

        assertNotNull(map, "Map should not be null");
        assertEquals(2, map.size(), "Map should contain exactly two entries");
        assertEquals("Value1", map.get(SampleEnum.FIRST), "First value should match");
        assertEquals("Value2", map.get(SampleEnum.SECOND), "Second value should match");
    }
}
