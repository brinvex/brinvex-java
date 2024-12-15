package com.brinvex.java;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * See <a href="https://stackoverflow.com/a/11469731/1157213">https://stackoverflow.com/a/11469731/1157213</a>
 */
public class LimitedLinkedMap<K, V> extends LinkedHashMap<K, V> {

    private final int maxEntries;

    public LimitedLinkedMap(int maxEntries) {
        super(maxEntries * 10 / 7, 0.7f, false);
        this.maxEntries = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxEntries;
    }
}
