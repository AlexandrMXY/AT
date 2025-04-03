package ru.mephi.bakinaa.lab3.db.views;

import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.ResultSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultResultSet extends ResultSet {
    private List<ResultRow> data = new ArrayList<>();

    @Override
    public void addRow(List<SimpleObj> objs) {
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
