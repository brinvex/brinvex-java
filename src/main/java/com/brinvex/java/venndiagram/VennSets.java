package com.brinvex.java.venndiagram;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * See <a href="https://en.wikipedia.org/wiki/Venn_diagram">https://en.wikipedia.org/wiki/Venn_diagram</a>
 */
public class VennSets<VALUE> {

    private final Set<VALUE> left;
    private final Set<VALUE> right;

    private Set<VALUE> leftOnly;
    private Set<VALUE> rightOnly;

    private Set<VALUE> intersection;
    private Set<VALUE> union;

    public static <VALUE> VennSets<VALUE> of(
            Collection<VALUE> left, Collection<VALUE> right) {
        return new VennSets<>(new LinkedHashSet<>(left), new LinkedHashSet<>(right));
    }

    private VennSets(Set<VALUE> left, Set<VALUE> right) {
        this.left = left;
        this.right = right;
    }

    public Set<VALUE> left() {
        return left;
    }

    public Set<VALUE> right() {
        return right;
    }

    public Set<VALUE> leftOnly() {
        if (leftOnly == null) {
            leftOnly = new LinkedHashSet<>(left);
            leftOnly.removeAll(intersection());
        }
        return leftOnly;
    }

    public Set<VALUE> rightOnly() {
        if (rightOnly == null) {
            rightOnly = new LinkedHashSet<>(right);
            rightOnly.removeAll(intersection());
        }
        return rightOnly;
    }

    public Set<VALUE> intersection() {
        if (intersection == null) {
            intersection = new LinkedHashSet<>(left);
            intersection.retainAll(right);
        }
        return intersection;
    }

    public Set<VALUE> union() {
        if (union == null) {
            union = new LinkedHashSet<>(left);
            union.addAll(right);
        }
        return union;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Venn[", "]")
                .add("l=" + left)
                .add("r=" + right)
                .toString();
    }
}
