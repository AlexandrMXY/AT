package ru.mephi.bakinaa.regex.chars;

import ru.mephi.bakinaa.regex.chars.CharGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SymbolsTable implements Iterable<Integer> {
    public static final int UNKNOWN_CHAR = -1;
    public static final int EOL = 0;

    private List<CharGroup> groups;

    public SymbolsTable(CharGroup... groups) {
        this.groups = Arrays.asList(groups);
    }

    public int idOf(char c) {
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).isCharInsideGroup(c))
                return i + 1;
        }
        return UNKNOWN_CHAR;

    }

    public CharGroup getGroup(int id) {
        return groups.get(id - 1);
    }

    public int eol() {
        return EOL;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iter();
    }

    private class Iter implements Iterator<Integer> {
        int id = 0;

        @Override
        public boolean hasNext() {
            return id < groups.size();
        }

        @Override
        public Integer next() {
            id++;
            return id;
        }
    }
}
