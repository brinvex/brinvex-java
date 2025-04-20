package com.brinvex.java.collection;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class SortedListUtil {

    /**
     * Performs a binary search over a list of items using a projection function that maps items to comparable keys.
     *
     * @param l            the list to search (must be sorted by the key extracted with comparingBy)
     * @param key          the key to search for
     * @param comparingBy  function extracting a Comparable property from items
     * @param <T>          the type of list elements
     * @param <P>          the type of the property used for comparison (must be Comparable)
     * @return index of the key, or (-(insertion point) - 1) if not found
     * <p>
     * This method runs in log(n) time for a "random access" list.
     * See also ${{@link java.util.Collections#binarySearch(List, Object, Comparator)}
     */
    public static <T, P extends Comparable<? super P>> int binarySearch(
            List<? extends T> l, P key, Function<? super T, ? extends P> comparingBy) {

        int low = 0;
        int high = l.size() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = l.get(mid);
            P midProp = comparingBy.apply(midVal);
            int cmp = midProp.compareTo(key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found
    }

    public static <T, P extends Comparable<P>> List<T> headList(
            List<T> sortedList,
            Function<T, ? extends P> sortedBy,
            P toKey,
            boolean inclusive
    ) {
        int headIndex;
        if (inclusive) {
            headIndex = keyInclusiveHeadLength(sortedList, sortedBy, toKey);
        } else {
            headIndex = keyExclusiveHeadLength(sortedList, sortedBy, toKey);
        }
        return sortedList.subList(0, headIndex);
    }

    public static <T, P extends Comparable<P>> int headLength(
            List<T> sortedList,
            Function<T, ? extends P> sortedBy,
            P toKey,
            boolean inclusive
    ) {
        if (inclusive) {
            return keyInclusiveHeadLength(sortedList, sortedBy, toKey);
        } else {
            return keyExclusiveHeadLength(sortedList, sortedBy, toKey);
        }
    }

    public static <T, P extends Comparable<P>> int keyExclusiveHeadLength(
            List<T> sortedList,
            Function<T, ? extends P> sortedBy,
            P toKey
    ) {
        int index = binarySearch(sortedList, toKey, sortedBy);
        if (index < 0) {
            index = -index - 1;
        } else {
            // move past duplicates if any
            do {
                index--;
            } while (index >= 0 && sortedBy.apply(sortedList.get(index)).compareTo(toKey) == 0);
            index++;
        }
        return index;
    }

    public static <T, P extends Comparable<P>> int keyInclusiveHeadLength(
            List<T> sortedList,
            Function<T, ? extends P> sortedBy,
            P toKey
    ) {
        int index = binarySearch(sortedList, toKey, sortedBy);
        if (index < 0) {
            index = -index - 1;
        } else {
            // move past duplicates if any
            int size = sortedList.size();
            do {
                index++;
            } while (index < size && sortedBy.apply(sortedList.get(index)).compareTo(toKey) == 0);
        }
        return index;
    }

    public static <T, P extends Comparable<P>> List<T> tailList(
            List<T> sortedList,
            Function<T, ? extends P> sortedBy,
            P fromKey,
            boolean inclusive
    ) {
        int tailIndex;
        if (inclusive) {
            tailIndex = keyInclusiveTailIndex(sortedList, sortedBy, fromKey);
        } else {
            tailIndex = keyExclusiveTailIndex(sortedList, sortedBy, fromKey);
        }
        return sortedList.subList(tailIndex, sortedList.size());
    }

    public static <T, P extends Comparable<P>> int tailIndex(
            List<T> sortedList,
            Function<T, ? extends P> sortedBy,
            P fromKey,
            boolean inclusive
    ) {
        if (inclusive) {
            return keyInclusiveTailIndex(sortedList, sortedBy, fromKey);
        } else {
            return keyExclusiveTailIndex(sortedList, sortedBy, fromKey);
        }
    }

    public static <T, P extends Comparable<P>> int keyExclusiveTailIndex(
            List<T> sortedList,
            Function<T, ? extends P> sortedBy,
            P fromKey
    ) {
        int index = binarySearch(sortedList, fromKey, sortedBy);
        if (index < 0) {
            // insertion point = first element > fromKey
            index = -index - 1;
        } else {
            // move past all equal-fromKey duplicates to land on first > fromKey
            int size = sortedList.size();
            do {
                index++;
            } while (index < size
                     && sortedBy.apply(sortedList.get(index)).compareTo(fromKey) == 0);
        }
        return index;
    }

    public static <T, P extends Comparable<P>> int keyInclusiveTailIndex(
            List<T> sortedList,
            Function<T, ? extends P> sortedBy,
            P fromKey
    ) {
        int index = binarySearch(sortedList, fromKey, sortedBy);
        if (index < 0) {
            // if no exact match, insertion point is first > fromKey,
            // and since there is no equal, that's where >= would start too
            index = -index - 1;
        } else {
            // back up to the first occurrence of fromKey
            do {
                index--;
            } while (index >= 0
                     && sortedBy.apply(sortedList.get(index)).compareTo(fromKey) == 0);
            index++;
        }
        return index;
    }

}
