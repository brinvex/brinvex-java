package com.brinvex.java;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class AccentUtil {

    private static class LazyHolder {
        private static final String EMPTY = "";
        private static final Pattern STRIP_ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    }

    public static String stripAccents(final String input) {
        if (input == null) {
            return null;
        }
        final StringBuilder decomposed = new StringBuilder(Normalizer.normalize(input, Normalizer.Form.NFD));
        convertRemainingAccentCharacters(decomposed);
        // Note that this doesn't correctly remove ligatures...
        return LazyHolder.STRIP_ACCENTS_PATTERN.matcher(decomposed).replaceAll(LazyHolder.EMPTY);
    }

    private static void convertRemainingAccentCharacters(final StringBuilder decomposed) {
        for (int i = 0; i < decomposed.length(); i++) {
            if (decomposed.charAt(i) == 'Ł') {
                decomposed.setCharAt(i, 'L');
            } else if (decomposed.charAt(i) == 'ł') {
                decomposed.setCharAt(i, 'l');
            }
        }
    }
}
