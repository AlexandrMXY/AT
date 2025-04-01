package ru.mephi.bakinaa.lab3.db.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.db.constrints.*;
import ru.mephi.bakinaa.lab3.commons.SimpleObj;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class Table {
    private final Database database;

    private final String name;
    private final Columns columns = new Columns();
    private final List<Constraint> constraints = new ArrayList<>();

    private final List<Row> rows = new ArrayList<>();

    private List<Integer> pKey;

    public void setPKey(List<Integer> pKeyCols) {
        addConstraint(new PrimaryKeyConstraint(pKeyCols));
        pKey = pKeyCols;
    }

    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
    }

    public Row getById(Row keyRow) {
        int hash = keyRow.hash(pKey);
        for (Row row : rows) {
            if (row.hash(pKey) == hash && row.equals(keyRow, pKey))
                return row;
        }
        return null;
    }

    public Row getRow(int id) {
        return rows.get(id);
    }

    public Row findAny(Row expected, List<Integer> cols) {
        int hash = expected.hash(cols);
        for (Row row : rows) {
            if (row.hash(cols) == hash && row.equals(expected, cols))
                return row;
        }
        return null;
    }

    public boolean existsByCols(Row expected, List<Integer> cols) {
        return findAny(expected, cols) != null;
    }

    public void insert(Row row) {
        for (Constraint c : constraints)
            if (!c.checkOnInsert(this, row))
                throw new InvalidDBAccessException("Unable to insert row: row volatiles constraint " + c.toString());
        rows.add(row);
    }

    public void insert(Map<String, SimpleObj> rowData) {
        Row row = new Row();
        rowData.forEach((col, val) -> {
            int index = columns.getIndex(col);
            if (index < 0)
                throw new InvalidDBAccessException("Unknown column " + col);
            row.set(index, val);
        });
        insert(row);
    }

    public int getRowsCnt() {
        return rows.size();
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("==== Table ").append(name).append(" ====\n");
        builder.append("Columns:\n").append(columns);
        builder.append("Constraints:\n");
        for (Constraint constraint : constraints) {
            switch (constraint) {
                case UniqueConstraint c -> {
                    builder.append(c.getClass().getSimpleName()).append("(");
                    builder.append(c.getCols().stream()
                            .map(i -> columns.getColumn(i).getName())
                            .collect(Collectors.joining(", ")));
                    builder.append(")\n");
                }
                case ForeignKeyConstraint c -> {
                    builder .append(c.getFromTable().name).append("::")
                            .append(c.getFromTable().columns.getColumn(c.getFromCol()).getName())
                            .append(" -> ")
                            .append(c.getToTable().name).append("::")
                            .append(c.getToTable().columns.getColumn(c.getToCol()).getName())
                            .append("\n");
                }
                case NotNullConstraint c -> {
                    builder.append("NotNull(").append(columns.getColumn(c.getRowIndex()).getName()).append(")\n");
                }
                default ->
                    builder.append(constraint.toString()).append("\n");
            }
        }

        return builder.toString();
    }
}

