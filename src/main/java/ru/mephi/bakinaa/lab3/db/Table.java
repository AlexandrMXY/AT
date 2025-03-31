package ru.mephi.bakinaa.lab3.db;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.db.constrints.Constraint;
import ru.mephi.bakinaa.lab3.db.objects.Obj;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Table {
    private final Database database;

    private final String name;
    private final Columns columns = new Columns();
    private final List<Constraint> constraints = new ArrayList<>();

    private final List<Row> rows = new ArrayList<>();

    private List<Integer> pKey = new ArrayList<>();

    public Row getById(Row keyRow) {
        int hash = keyRow.hash(pKey);
        for (Row row : rows) {
            if (row.hash(pKey) == hash && row.equals(keyRow, pKey))
                return row;
        }
        return null;
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
}

