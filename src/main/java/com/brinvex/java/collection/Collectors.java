package com.brinvex.java.collection;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Private members copied from {@link java.util.stream.Collectors} OracleJDK 17.0.6
 */
public class Collectors {

    private static final Set<Collector.Characteristics> CH_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
    static final Set<Collector.Characteristics> CH_UNORDERED_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED,
            Collector.Characteristics.IDENTITY_FINISH));

    private record CollectorImpl<T, A, R>(
            Supplier<A> supplier,
            BiConsumer<A, T> accumulator,
            BinaryOperator<A> combiner,
            Function<A, R> finisher,
            Set<Collector.Characteristics> characteristics
    ) implements Collector<T, A, R> {

        CollectorImpl(
                Supplier<A> supplier,
                BiConsumer<A, T> accumulator,
                BinaryOperator<A> combiner,
                Set<Collector.Characteristics> characteristics
        ) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }
    }

    @SuppressWarnings("unchecked")
    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    private static <T, K, V> BiConsumer<Map<K, V>, T> uniqKeysMapAccumulator(
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends V> valueMapper
    ) {
        return (map, element) -> {
            K k = keyMapper.apply(element);
            V v = Objects.requireNonNull(valueMapper.apply(element));
            V u = map.putIfAbsent(k, v);
            if (u != null) throw duplicateKeyException(k, u, v);
        };
    }

    private static <K, V, M extends Map<K, V>> BinaryOperator<M> uniqKeysMapMerger() {
        return (m1, m2) -> {
            for (Map.Entry<K, V> e : m2.entrySet()) {
                K k = e.getKey();
                V v = Objects.requireNonNull(e.getValue());
                V u = m1.putIfAbsent(k, v);
                if (u != null) throw duplicateKeyException(k, u, v);
            }
            return m1;
        };
    }

    private static IllegalStateException duplicateKeyException(
            Object k, Object u, Object v
    ) {
        return new IllegalStateException(String.format(
                "Duplicate key %s (attempted merging values %s and %s)",
                k, u, v));
    }

    public static <T, K, U> Collector<T, ?, LinkedHashMap<K, U>> toLinkedMap(
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends U> valueMapper
    ) {
        return new CollectorImpl<>(LinkedHashMap::new,
                uniqKeysMapAccumulator(keyMapper, valueMapper),
                uniqKeysMapMerger(),
                CH_ID);
    }

    public static <T, K, U> Collector<T, ?, LinkedHashMap<K, U>> toHashMap(
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends U> valueMapper
    ) {
        return new CollectorImpl<>(HashMap::new,
                uniqKeysMapAccumulator(keyMapper, valueMapper),
                uniqKeysMapMerger(),
                CH_ID);
    }

    public static <T, K, U> Collector<T, ?, TreeMap<K, U>> toTreeMap(
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends U> valueMapper
    ) {
        return new CollectorImpl<>(TreeMap::new,
                uniqKeysMapAccumulator(keyMapper, valueMapper),
                uniqKeysMapMerger(),
                CH_ID);
    }

    public static <T, K extends Enum<K>, U> Collector<T, ?, EnumMap<K, U>> toEnumMap(
            Class<K> enumType,
            Function<? super T, K> keyMapper,
            Function<? super T, ? extends U> valueMapper
    ) {
        return new CollectorImpl<>(() -> new EnumMap<>(enumType),
                uniqKeysMapAccumulator(keyMapper, valueMapper),
                uniqKeysMapMerger(),
                CH_ID);
    }

    public static <T> Collector<T, ?, Set<T>> toHashSet() {
        return new CollectorImpl<>(HashSet::new, Set::add,
                (left, right) -> {
                    if (left.size() < right.size()) {
                        right.addAll(left);
                        return right;
                    } else {
                        left.addAll(right);
                        return left;
                    }
                },
                CH_UNORDERED_ID);
    }

    public static <T> Collector<T, ?, Set<T>> toLinkedSet() {
        return new CollectorImpl<>(LinkedHashSet::new, Set::add,
                (left, right) -> {
                    if (left.size() < right.size()) {
                        right.addAll(left);
                        return right;
                    } else {
                        left.addAll(right);
                        return left;
                    }
                },
                CH_UNORDERED_ID);
    }

}
