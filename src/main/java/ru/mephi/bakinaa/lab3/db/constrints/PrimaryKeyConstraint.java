package ru.mephi.bakinaa.lab3.db.constrints;

import ru.mephi.bakinaa.lab3.db.Row;
import ru.mephi.bakinaa.lab3.db.Table;
import ru.mephi.bakinaa.lab3.db.objects.Obj;

import java.util.List;
import java.util.Map;

public class PrimaryKeyConstraint extends UniqueConstraint {
    public PrimaryKeyConstraint(List<Integer> cols) {
        super(cols);
    }

    @Override
    public boolean checkOnInsert(Table table, Row row) {
        return checkNonNull(row) && super.checkOnInsert(table, row);
    }

    @Override
    public boolean checkOnModify(Table table, Row row, Map<Integer, Obj> updates) {
        return checkNonNull(row) && super.checkOnModify(table, row, updates);
    }

    private boolean checkNonNull(Row row) {
        for (int i : cols)
            if (row.get(i) != null)
                return true;
        return false;
    }
}
