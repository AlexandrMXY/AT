package ru.mephi.bakinaa.lab3.db;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import lombok.AllArgsConstructor;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;

import java.util.*;

public class ResultSet {
    private static final Map<Id, Integer> mapping = new HashMap<>();
    private static final Comparator<ResultRow> DEFAULT_COMPARATOR = Comparator.comparingInt(a -> a.mark);
    private final SortedSet<ResultRow> data = new TreeSet<>(DEFAULT_COMPARATOR);
    private int nextMark = 0;
    private int nextIndex = 0;

    public int getOrCreateIndex(Id id) {
        if (mapping.containsKey(id))
            return mapping.get(id);
        mapping.put(id, nextIndex);
        return nextIndex++;
    }

    public void addRow(List<SimpleObj> objs) {
        data.add(new ResultRow(nextMark++, objs));
    }

    @AllArgsConstructor
    private static class ResultRow {
        public int mark;
        public List<SimpleObj> data = new ArrayList<>();
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (ResultRow row : data) {
            for (Obj o : row.data) {
                builder.append(o).append(" ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
