package test.com.brinvex.java;

import com.brinvex.java.RandomUtil;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomUtilTest {

    @Test
    public void randomDoublesSummingToOne() {
        Random random = new Random();
        {
            double[] randDoubles = RandomUtil.randomDoublesSummingToOne(1, random::nextDouble);
            assertEquals(1, randDoubles.length);
            assertEquals(1.0, randDoubles[0]);
        }
        {
            double[] randDoubles = RandomUtil.randomDoublesSummingToOne(2, random::nextDouble);
            assertEquals(2, randDoubles.length);
            assertTrue(randDoubles[0] >= 0);
            assertTrue(randDoubles[0] <= 1);
            assertTrue(randDoubles[1] >= 0);
            assertTrue(randDoubles[1] <= 1);
            assertEquals(1.0, randDoubles[0] + randDoubles[1]);
        }
        {
            double[] randDoubles = RandomUtil.randomDoublesSummingToOne(3, random::nextDouble);
            assertEquals(3, randDoubles.length);
            assertTrue(randDoubles[0] >= 0);
            assertTrue(randDoubles[0] <= 1);
            assertTrue(randDoubles[1] >= 0);
            assertTrue(randDoubles[1] <= 1);
            assertTrue(randDoubles[2] >= 0);
            assertTrue(randDoubles[2] <= 1);
            assertEquals(1.0, randDoubles[0] + randDoubles[1] + randDoubles[2]);
        }
        {
            double[] randDoubles = RandomUtil.randomDoublesSummingToOne(4, random::nextDouble);
            assertEquals(4, randDoubles.length);
            assertTrue(randDoubles[0] >= 0);
            assertTrue(randDoubles[0] <= 1);
            assertTrue(randDoubles[1] >= 0);
            assertTrue(randDoubles[1] <= 1);
            assertTrue(randDoubles[2] >= 0);
            assertTrue(randDoubles[2] <= 1);
            assertTrue(randDoubles[3] >= 0);
            assertTrue(randDoubles[3] <= 1);
            assertEquals(1.0, randDoubles[0] + randDoubles[1] + randDoubles[2] + randDoubles[3]);
        }

    }
}
