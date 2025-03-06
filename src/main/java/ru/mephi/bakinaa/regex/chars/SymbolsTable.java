package ru.mephi.bakinaa.regex.chars;

import lombok.Getter;
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
        return groups.get(id - 1);
    }

    public int eol() {
        return EOL;
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
