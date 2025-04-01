package ru.mephi.bakinaa.lab3.db.constrints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.db.Row;
import ru.mephi.bakinaa.lab3.db.Table;
import ru.mephi.bakinaa.lab3.db.objects.Obj;

import java.util.Map;

@AllArgsConstructor
@Getter
public class NotNullConstraint extends Constraint {
    private final int rowIndex;

    @Override
    public boolean checkOnInsert(Table table, Row row) {
        return row.get(rowIndex) != null;
    }

    @Override
    public boolean checkOnModify(Table table, Row row, Map<Integer, Obj> updates) {
        return !updates.containsKey(rowIndex) || updates.get(rowIndex) != null;
    }
}
