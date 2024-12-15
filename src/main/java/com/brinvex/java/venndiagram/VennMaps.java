package com.brinvex.java.venndiagram;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;

import static com.brinvex.java.collection.Collectors.toLinkedMap;
import static java.util.function.Function.identity;

/**
 * See <a href="https://en.wikipedia.org/wiki/Venn_diagram">https://en.wikipedia.org/wiki/Venn_diagram</a>
 */
public class VennMaps<KEY, LEFT_VALUE, RIGHT_VALUE> {

    private final Map<KEY, LEFT_VALUE> left;
    private final Map<KEY, RIGHT_VALUE> right;

    private Map<KEY, LEFT_VALUE> leftOnly;
    private Map<KEY, RIGHT_VALUE> rightOnly;

    private Set<KEY> intersectionKeys;
    private Set<KEY> unionKeys;

    private Map<KEY, LEFT_VALUE> intersectionWithLeftValues;
    private Map<KEY, RIGHT_VALUE> intersectionWithRightValues;

    public static <KEY, LEFT_VALUE, RIGHT_VALUE> VennMaps<KEY, LEFT_VALUE, RIGHT_VALUE> of(Map<KEY, LEFT_VALUE> left, Map<KEY, RIGHT_VALUE> right) {
        return new VennMaps<>(new LinkedHashMap<>(left), new LinkedHashMap<>(right));
    }

    public static <KEY, LEFT_VALUE, RIGHT_VALUE> VennMaps<KEY, LEFT_VALUE, RIGHT_VALUE> of(
            Collection<LEFT_VALUE> left, Function<LEFT_VALUE, KEY> leftKeyMapper, Collection<RIGHT_VALUE> right, Function<RIGHT_VALUE, KEY> rightKeyMapper) {
        Map<KEY, LEFT_VALUE> leftMap = left.stream().collect(toLinkedMap(leftKeyMapper, identity()));
        Map<KEY, RIGHT_VALUE> rightMap = right.stream().collect(toLinkedMap(rightKeyMapper, identity()));
        return new VennMaps<>(leftMap, rightMap);
    }

    public static <KEY, VALUE> VennMaps<KEY, VALUE, VALUE> of(
            Collection<VALUE> left, Collection<VALUE> right, Function<VALUE, KEY> keyMapper) {
        Map<KEY, VALUE> leftMap = left.stream().collect(toLinkedMap(keyMapper, identity()));
        Map<KEY, VALUE> rightMap = right.stream().collect(toLinkedMap(keyMapper, identity()));
        return new VennMaps<>(leftMap, rightMap);
    }

    public static <VALUE> VennMaps<VALUE, VALUE, VALUE> of(
            Collection<VALUE> left, Collection<VALUE> right) {
        Map<VALUE, VALUE> leftMap = left.stream().collect(toLinkedMap(identity(), identity()));
        Map<VALUE, VALUE> rightMap = right.stream().collect(toLinkedMap(identity(), identity()));
        return new VennMaps<>(leftMap, rightMap);
    }

    private VennMaps(Map<KEY, LEFT_VALUE> left, Map<KEY, RIGHT_VALUE> right) {
        this.left = left;
        this.right = right;
    }

    public Map<KEY, LEFT_VALUE> left() {
        return left;
    }

    public Map<KEY, RIGHT_VALUE> right() {
        return right;
    }

    public Map<KEY, LEFT_VALUE> leftOnly() {
        if (leftOnly == null) {
            leftOnly = new LinkedHashMap<>(left);
            leftOnly.keySet().removeAll(intersectionKeys());
        }
        return leftOnly;
    }

    public Map<KEY, RIGHT_VALUE> rightOnly() {
        if (rightOnly == null) {
            rightOnly = new LinkedHashMap<>(right);
            rightOnly.keySet().removeAll(intersectionKeys());
        }
        return rightOnly;
    }

    public Set<KEY> intersectionKeys() {
        if (intersectionKeys == null) {
            intersectionKeys = new LinkedHashSet<>(left.keySet());
            intersectionKeys.retainAll(right.keySet());
        }
        return intersectionKeys;
    }

    public Set<KEY> unionKeys() {
        if (unionKeys == null) {
            unionKeys = new LinkedHashSet<>(left.keySet());
            unionKeys.addAll(right.keySet());
        }
        return unionKeys;
    }

    public Map<KEY, LEFT_VALUE> intersectionWithLeftValues() {
        if (intersectionWithLeftValues == null) {
            intersectionWithLeftValues = intersectionKeys()
                    .stream()
                    .collect(toLinkedMap(identity(), left::get));
        }
        return intersectionWithLeftValues;
    }

    public Map<KEY, RIGHT_VALUE> intersectionWithRightValues() {
        if (intersectionWithRightValues == null) {
            intersectionWithRightValues = intersectionKeys()
                    .stream()
                    .collect(toLinkedMap(identity(), right::get));
        }
        return intersectionWithRightValues;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Venn[", "]")
                .add("l=" + left)
                .add("r=" + right)
                .toString();
    }
}
