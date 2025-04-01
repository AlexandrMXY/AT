package ru.mephi.bakinaa.lab3.db.constrints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.db.core.Row;
import ru.mephi.bakinaa.lab3.db.core.Table;
import ru.mephi.bakinaa.lab3.commons.SimpleObj;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ForeignKeyConstraint extends Constraint {
    private final Table fromTable;
    private final int fromCol;
    private final Table toTable;
    private final int toCol;


    @Override
    public boolean checkOnInsert(Table table, Row row) {
        if (table == fromTable)
            return toTable.existsByCols(Row.withCol(toCol, row.get(fromCol)), List.of(toCol));
        return true;
    }

    @Override
    public boolean checkOnRemove(Table table, Row row) {
        if (table == toTable)
            return !toTable.existsByCols(Row.withCol(fromCol, row.get(toCol)), List.of(fromCol));
        return true;
    }

    @Override
    public boolean checkOnModify(Table table, Row row, Map<Integer, SimpleObj> updates) {
        if (table == fromTable) {
            return toTable.existsByCols(
                    Row.withCol(toCol,
                            updates.getOrDefault(fromCol, row.get(fromCol))),
                    List.of(toCol));
        } else {
            return !toTable.existsByCols(
                    Row.withCol(fromCol,
                            updates.getOrDefault(toCol, row.get(toCol))),
                    List.of(fromCol));
        }
    }

}
