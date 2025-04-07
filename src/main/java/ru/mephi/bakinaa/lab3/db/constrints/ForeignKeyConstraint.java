package ru.mephi.bakinaa.lab3.db.constrints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.db.relations.rows.Row;
import ru.mephi.bakinaa.lab3.db.relations.Table;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class ForeignKeyConstraint extends Constraint {
    private final Table fromTable;
    private final int fromCol;
    private final Table toTable;
    private final int toCol;

    public ForeignKeyConstraint(String name, Table fromTable, int fromCol, Table toTable, int toCol) {
        super(name);
        this.fromTable = fromTable;
        this.fromCol = fromCol;
        this.toTable = toTable;
        this.toCol = toCol;
    }

    @Override
    public boolean checkOnInsert(Table table, Row row) {
        if (table == fromTable)
            return toTable.existsByCols(Row.withCol(toCol, row.get(fromCol)), Set.of(toCol));
        return true;
    }

    @Override
    public boolean checkOnRemove(Table table, Row row) {
        if (table == toTable)
            return !toTable.existsByCols(Row.withCol(fromCol, row.get(toCol)), Set.of(fromCol));
        return true;
    }

    @Override
    public boolean checkOnModify(Table table, Row row, Map<Integer, SimpleObj> updates) {
        if (table == fromTable) {
            return toTable.existsByCols(
                    Row.withCol(toCol,
                            updates.getOrDefault(fromCol, row.get(fromCol))),
                    Set.of(toCol));
        } else {
            return !toTable.existsByCols(
                    Row.withCol(fromCol,
                            updates.getOrDefault(toCol, row.get(toCol))),
                    Set.of(fromCol));
        }
    }

    @Override
    public boolean checkOnTableRemove(Table table) {
        return table != fromTable && table != toTable;
    }
}
