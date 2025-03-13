package ru.mephi.bakinaa.regex.chars;

import lombok.NonNull;
import lombok.ToString;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public record CharGroup  (
    char from,
    char to
) implements Iterable<Character> {


    public CharGroup(char c) {
        this(c, c);
    }

    public CharGroup(char from, char to) {
        this.from = from <= to ? from : to;
        this.to = from >= to ? from : to;
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

//    public List<CharGroup> splitIntersect(CharGroup second) {
//        System.out.printf("[%c, %c] [%c, %c] ->", from, to, second.from, second.to);
//        var res = splitIntersect0(second);
//        for (CharGroup r : res)
//            System.out.printf(" [%c, %c]", r.from, r.to);
//        System.out.println();
//        return res;
//    }


    public List<CharGroup> splitIntersect(CharGroup second) {
        if (compare(second) != CompareResult.INTERSECT)
            throw new IllegalArgumentException();

        if (second.from == second.to)
            return second.splitIntersect(this);

        if (from == to) {
            if (from == second.from) {
                return List.of(
                        new CharGroup(from, from),
                        new CharGroup((char) (from + 1), second.to));
            }
            if (from == second.to) {
                return List.of(
                        new CharGroup(second.from, (char) (second.to - 1)),
                        new CharGroup(to, to));
            }
            return List.of(
                    new CharGroup(second.from, (char) (from - 1)),
                    new CharGroup(from, from),
                    new CharGroup((char) (from + 1), second.to));
        }

        if (from == second.to)
            return second.splitIntersect(this);
        if (to == second.from) {
            return List.of(
                    new CharGroup(from, (char) (to - 1)),
                    new CharGroup(to, to),
                    new CharGroup((char) (to + 1), second.to));
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

    @Override
    @NonNull
    public Iterator<Character> iterator() {
        return new Iter();
    }

    private class Iter implements Iterator<Character> {
        char cur = from;

        @Override
        public boolean hasNext() {
            return cur < to;
        }

        @Override
        public Character next() {
            return cur++;
        }
    }
}
