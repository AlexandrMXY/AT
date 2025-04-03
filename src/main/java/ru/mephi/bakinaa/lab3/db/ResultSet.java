package ru.mephi.bakinaa.lab3.db;

import lombok.AllArgsConstructor;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;

import java.util.*;

public abstract class ResultSet implements Iterable<ResultSet.ResultRow> {
    protected static final Map<Id, Integer> mapping = new HashMap<>();

    private int nextIndex = 0;

    public int getOrCreateIndex(Id id) {
        if (mapping.containsKey(id))
            return mapping.get(id);
        mapping.put(id, nextIndex);
        return nextIndex++;
    }

    public abstract void addRow(List<SimpleObj> objs);

    @AllArgsConstructor
    public static class ResultRow {
        public List<SimpleObj> data;
    }
}
