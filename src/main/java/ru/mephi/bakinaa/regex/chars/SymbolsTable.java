package ru.mephi.bakinaa.regex.chars;

import lombok.Getter;
import lombok.NonNull;
import ru.mephi.bakinaa.regex.chars.CharGroup;

import java.util.*;

public class SymbolsTable implements Iterable<Integer> {
    public static final int UNKNOWN_CHAR = -1;
    public static final int EOL = 0;

    @Getter private final List<CharGroup> groups = new ArrayList<>();
    @Getter private final Map<Integer, Integer> indexCharTable = new HashMap<>();

    private int nextTreeIndex = 1;

    public int idOf(char c) {
        // Collections.binarySearch()
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).isCharInsideGroup(c))
                return i + 1;
        }
        return UNKNOWN_CHAR;

    }

    public CharGroup getGroup(int id) {
        if (id < 1)
            return null;
        return groups.get(id - 1);
    }

    public int eol() {
        return EOL;
    }

    public int lastGroupId() {
        return groups.size();
    }

    public int ifOf(CharGroup g) {
        if (g == null)
            return 0;
        int id = idOf(g.from());
        if (id == UNKNOWN_CHAR)
            return UNKNOWN_CHAR;
        if (!getGroup(id).isCharInsideGroup(g.to()))
            return UNKNOWN_CHAR;
        return id;
    }

    public void registerGroup(CharGroup group) {
        for (int i = 0; i < groups.size(); i++) {
            CharGroup.CompareResult compareResult = group.compare(groups.get(i));
            if (compareResult == CharGroup.CompareResult.LESS) {
                groups.add(i, group);
                return;
            }
            if (compareResult == CharGroup.CompareResult.EQUAL) {
                return;
            }
            if (compareResult == CharGroup.CompareResult.INTERSECT) {
                CharGroup prev = groups.remove(i);
                groups.addAll(i, prev.splitIntersect(group));
                return;
            }
        }
        groups.add(group);
    }

    public void registerGroups(List<CharGroup> groups) {
        for (CharGroup g : groups)
            registerGroup(g);
    }

    public int nextTreeIndex(int charId) {
        int index = nextTreeIndex++;
        indexCharTable.put(index, charId);
        return index;
    }

    public int getCharIdOfTreeIndex(int treeIndex) {
        return indexCharTable.get(treeIndex);
    }

    public int getLastTreeIndex() {
        return nextTreeIndex - 1;
    }

    public SymbolsTable multiply(SymbolsTable other) {
        SymbolsTable res = new SymbolsTable();
        for (var g : groups)
            res.registerGroup(g);
        for (var g : other.groups)
            res.registerGroup(g);
        return res;
    }

    public String charAsString(int charId) {
        if (charId == 0)
            return "$";
        CharGroup g = groups.get(charId - 1);
        if (g.from() == g.to())
            return String.valueOf(g.from());
        return "[" + escapedChar(g.from()) + "-" + escapedChar(g.to()) + "]";
    }

    private String escapedChar(char c) {
        if (c == '%' || c == '-' || c == ']')
            return "%" + c;
        return String.valueOf(c);
    }

    public Set<Integer> getIdsOf(CharGroup g) {
        Set<Integer> res = new HashSet<>();
        char firstUnconsumed = g.from();
        while (firstUnconsumed <= g.to()) {
            int index = idOf(firstUnconsumed);
            if (index < 1)
                return null;
            res.add(index);
            firstUnconsumed = (char) (getGroup(index).to() + 1);
        }
        return res;
    }

    @Override
    @NonNull
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
