package test.com.brinvex.java;

import com.brinvex.java.collection.CollectionPrintUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CollectionPrintUtilTest {

    @Test
    void prettyPrintCollection() {
        record Item(String name, BigDecimal price) {
        }
        List<Item> items = List.of(
                new Item("apple", new BigDecimal("1.5")),
                new Item("kiwi", new BigDecimal("1E+1"))
        );
        List<Function<Item, Object>> projections = List.of(Item::name, Item::price);

        String s = CollectionPrintUtil.prettyPrintCollection(items, List.of("name", "price"), projections);
        assertEquals("""
                 name; price
                apple;   1.5
                 kiwi;    10
                """, s);
    }

    @Test
    void prettyPrintCollection_equalItems() {
        record Item(String name, int qty) {
        }
        List<Item> items = List.of(
                new Item("a", 1),
                new Item("a", 1),
                new Item("b", 2)
        );
        List<Function<Item, Object>> projections = List.of(Item::name, Item::qty);

        String s = CollectionPrintUtil.prettyPrintCollection(items, List.of("name", "qty"), projections);
        assertEquals("""
                name; qty
                   a;   1
                   a;   1
                   b;   2
                """, s);
    }
}
