package ru.mephi.bakinaa.lab3.db.constrints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.db.relations.rows.Row;
import ru.mephi.bakinaa.lab3.db.relations.Table;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;

import java.util.Map;

@Getter
public class NotNullConstraint extends Constraint {
    private final int rowIndex;

    public NotNullConstraint(String name, int rowIndex) {
        super(name);
        this.rowIndex = rowIndex;
    }

    @Override
    public boolean checkOnInsert(Table table, Row row) {
        return row.get(rowIndex) != null;
    }

    @Override
    public boolean checkOnModify(Table table, Row row, Map<Integer, SimpleObj> updates) {
        return !updates.containsKey(rowIndex) || updates.get(rowIndex) != null;
    }
}
