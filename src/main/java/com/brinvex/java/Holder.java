package com.brinvex.java;

import java.util.Objects;

public final class Holder<T> {
    private T value;

    public Holder(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Holder{%s}".formatted(value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Holder<?> holder)) return false;
        return Objects.equals(value, holder.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
