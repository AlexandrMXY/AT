package ru.mephi.bakinaa.regex.chars;

import com.kitfox.svg.A;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ToString
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

    public CompareResult compare(CharGroup second) {
        if (to < second.from)
            return CompareResult.LESS;
        if (from > second.to)
            return CompareResult.GREATER;
        if (from == second.from && to == second.to)
            return CompareResult.EQUAL;
        return CompareResult.INTERSECT;
    }

    public List<CharGroup> splitIntersect(CharGroup second) {
        if (compare(second) != CompareResult.INTERSECT)
            throw new IllegalArgumentException();

        if (second.from == second.to)
            return second.splitIntersect(this);

        if (from == to && from == second.from) {
            return List.of(
                    new CharGroup(from, from),
                    new CharGroup((char) (from + 1), second.to));
        }
        if (from == to && from == second.to) {
            return List.of(
                    new CharGroup(second.from, (char) (second.to - 1)),
                    new CharGroup(to, to));
        }

        char[] points = new char[] {from, to, second.from, second.to};
        Arrays.sort(points);

        return List.of(
                new CharGroup(points[0], points[1]),
                new CharGroup((char) (points[1] + 1), points[2]),
                new CharGroup((char) (points[2] + 1), points[3])
        );
    }

    public enum CompareResult {
        LESS,
        GREATER,
        EQUAL,
        INTERSECT
    }
}
