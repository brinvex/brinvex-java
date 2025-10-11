package com.brinvex.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;

public class StringUtil {

    @SuppressWarnings("SpellCheckingInspection")
    public static class Constants {
        public static final char[] DIGITS_CHARS = "0123456789".toCharArray();
        public static final char[] UP_LETTER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        public static final char[] UP_LETTER_AND_DIGIT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    }

    private static class LazyHolder {
        private static final String EMPTY = "";
        private static final String SPACE = " ";
        private static final Pattern FORMAT_CHARACTER_PATTERN = Pattern.compile("\\p{Cf}");
        private static final Pattern EMOJI_PRESENTATION_PATTERN = Pattern.compile("\\p{IsEmoji_Presentation}");
    }

    public static String deleteAllWhitespaces(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        final int sz = str.length();
        final char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        if (count == 0) {
            return LazyHolder.EMPTY;
        }
        return new String(chs, 0, count);
    }

    public static String deleteAllWhitespacesAndSpaceChars(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        final int sz = str.length();
        final char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c) && !Character.isSpaceChar(c)) {
                chs[count++] = c;
            }
        }
        if (count == sz) {
            return str;
        }
        if (count == 0) {
            return LazyHolder.EMPTY;
        }
        return new String(chs, 0, count);
    }

    public static String stripToNull(String s) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        if (s.isEmpty()) {
            return null;
        }
        return s;
    }

    public static String stripToEmpty(String s) {
        if (s == null) {
            return LazyHolder.EMPTY;
        }
        s = s.trim();
        if (s.isEmpty()) {
            return LazyHolder.EMPTY;
        }
        return s;
    }

    public static String deleteFormatCharacters(String str) {
        if (str == null) {
            return null;
        }
        return LazyHolder.FORMAT_CHARACTER_PATTERN.matcher(str).replaceAll("");
    }

    public static String deleteEmojiPresentations(String str) {
        if (str == null) {
            return null;
        }
        return LazyHolder.EMOJI_PRESENTATION_PATTERN.matcher(str).replaceAll("");
    }

    public static String deleteTags(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("<[^>]*>", "");
    }

    public static String replaceSuffix(String str, String oldSuffix, String newSuffix) {
        if (str == null) {
            return null;
        }
        if (str.endsWith(oldSuffix)) {
            str = str.substring(0, str.length() - oldSuffix.length());
            str = str + newSuffix;
        }
        return str;
    }

    public static boolean containsRegex(String str, String regex) {
        return Pattern.compile(regex).matcher(str).find();
    }

    public static String[] splitByPercentile(String s, int percentile) {
        if (percentile < 1 || percentile > 99) {
            throw new IllegalArgumentException("Invalid input: percentile=" + percentile);
        }
        if (s == null) {
            return null;
        }
        int length = s.length();
        if (length <= 1) {
            return new String[] {s, ""};
        }
        int delimiterIndex = (int) (length / 100.0 * percentile);
        while (delimiterIndex < length) {
            if (Character.isWhitespace(s.charAt(delimiterIndex))) {
                return new String[] {s.substring(0, delimiterIndex), s.substring(delimiterIndex)};
            }
            delimiterIndex++;
        }
        return new String[] {s, ""};
    }

    public static String cleanMultiWhitespaces(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        final int size = str.length();
        final char[] newChars = new char[size];
        int count = 0;
        int whitespacesCount = 0;
        boolean startWhitespaces = true;
        for (int i = 0; i < size; i++) {
            final char actualChar = str.charAt(i);
            final boolean isWhitespace = Character.isWhitespace(actualChar);
            if (isWhitespace) {
                if (whitespacesCount == 0 && !startWhitespaces) {
                    newChars[count++] = LazyHolder.SPACE.charAt(0);
                }
                whitespacesCount++;
            } else {
                startWhitespaces = false;
                newChars[count++] = (actualChar == 160 ? 32 : actualChar);
                whitespacesCount = 0;
            }
        }
        if (startWhitespaces) {
            return LazyHolder.EMPTY;
        }
        return new String(newChars, 0, count - (whitespacesCount > 0 ? 1 : 0)).trim();
    }

    public static List<String> generateWords(char[]... charRanges) {
        if (charRanges.length == 0) {
            return new ArrayList<>();
        }

        List<StringBuilder> words = new ArrayList<>();
        words.add(new StringBuilder());

        for (char[] charRange : charRanges) {
            if (charRange.length == 0) {
                continue;
            }
            List<StringBuilder> newWords = new ArrayList<>();
            for (StringBuilder word : words) {
                for (char c : charRange) {
                    newWords.add(new StringBuilder(word).append(c));
                }
            }
            words = newWords;
        }
        return words.stream().map(Objects::toString).collect(toCollection(ArrayList::new));
    }

}
