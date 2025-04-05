package ru.mephi.bakinaa.lab3.db.views__;

import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.ResultSet;

import java.util.*;

public class SortedResultSet extends ResultSet {
    private final SortedSet<ResultRow> data;

    public SortedResultSet(Comparator<ResultRow> comparator) {
        this.data = new TreeSet<>(comparator);
    }

    @Override
    public void addRow(List<SimpleObj> objs)  {
        data.add(new ResultRow(objs));
    }

    @Override
    public Iterator<ResultRow> iterator() {
        return data.iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (ResultRow row : data) {
            for (Obj o : row.data)
                builder.append(o).append(" ");
            builder.append("\n");
        }

        return builder.toString();
    }
}
