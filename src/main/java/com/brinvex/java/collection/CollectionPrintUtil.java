package com.brinvex.java.collection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CollectionPrintUtil {

    public static <E> String prettyPrintCollection(
            Collection<E> items,
            List<String> headers,
            List<Function<E, Object>> columnProjections
    ) {
        String lineSeparator = "\n";
        String valueSeparator = "; ";
        return prettyPrintCollection(items, headers, columnProjections, lineSeparator, valueSeparator);
    }

    public static <E> String prettyPrintCollection(
            Collection<E> items,
            List<String> headers,
            List<Function<E, Object>> columnProjections,
            String lineSeparator,
            String valueSeparator
    ) {
        StringBuilder sb = new StringBuilder();
        int colCount = headers.size();
        List<Integer> columnLengths = new ArrayList<>(headers.size());
        for (String header : headers) {
            columnLengths.add(header.length());
        }

        Map<E, List<String>> itemColumnStrings = new LinkedHashMap<>();
        for (E item : items) {
            List<String> itemStrings = new ArrayList<>(colCount);
            itemColumnStrings.put(item, itemStrings);
            for (int i = 0, columnProjectionsLength = columnProjections.size(); i < columnProjectionsLength; i++) {
                Function<E, Object> columnProjection = columnProjections.get(i);
                Object columnValue = columnProjection.apply(item);
                String columnString;
                if (columnValue instanceof BigDecimal) {
                    columnString = ((BigDecimal) columnValue).toPlainString();
                } else {
                    columnString = String.valueOf(columnValue);
                }
                itemStrings.add(columnString);
                columnLengths.set(i, Math.max(columnLengths.get(i), columnString.length()));
            }
        }
        {
            StringBuilder line = new StringBuilder();
            for (int c = 0; c < colCount; c++) {
                String header = headers.get(c);
                Integer colLength = columnLengths.get(c);
                if (c != 0) {
                    line.append(valueSeparator);
                }
                line.append(("%" + colLength + "s").formatted(header));
            }
            sb.append(line);
            sb.append(lineSeparator);
        }
        {
            for (List<String> itemStrings : itemColumnStrings.values()) {
                StringBuilder line = new StringBuilder();
                for (int c = 0; c < colCount; c++) {
                    if (c != 0) {
                        line.append(valueSeparator);
                    }
                    String itemString = itemStrings.get(c);
                    Integer colLength = columnLengths.get(c);
                    line.append(("%" + colLength + "s").formatted(itemString));
                }
                sb.append(line);
                sb.append(lineSeparator);
            }
        }

        return sb.toString();
    }

}
