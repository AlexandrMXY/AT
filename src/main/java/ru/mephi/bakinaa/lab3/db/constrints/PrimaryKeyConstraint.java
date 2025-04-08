package ru.mephi.bakinaa.lab3.db.constrints;

import ru.mephi.bakinaa.lab3.db.relations.rows.Row;
import ru.mephi.bakinaa.lab3.db.relations.Table;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrimaryKeyConstraint extends UniqueConstraint {
    public PrimaryKeyConstraint(String name, Table table, Set<Integer> cols) {
        super(name, table, cols);
    }

    @Override
    public boolean checkOnInsert(Table table, Row row) {
        return checkNonNull(row, EMPTY) && super.checkOnInsert(table, row);
    }

    @Override
    public boolean checkOnModify(Table table, Row row, Map<Integer, SimpleObj> updates) {
        return checkNonNull(row, updates) && super.checkOnModify(table, row, updates);
    }

    private boolean checkNonNull(Row row, Map<Integer, SimpleObj> updates) {
        for (int i : cols)
            if (updates.getOrDefault(i, row.get(i)) != null)
                return true;
        return false;
    }

    @Override
    public void remove() {
        table.forceRemovePKey();
        super.remove();
    }
}
