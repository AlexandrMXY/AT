package ru.mephi.bakinaa.regex.chars;

public class CharGroup {
    private final char from;
    private final char to;

    public CharGroup(char c) {
        from = c;
        to = c;
    }
    public CharGroup(char first, char second) {
        from = first <= second ? first : second;
        to = first >= second ? first : second;
    }

    public boolean isCharInsideGroup(char c) {
        return from <= c && c <= to;
    }
}
