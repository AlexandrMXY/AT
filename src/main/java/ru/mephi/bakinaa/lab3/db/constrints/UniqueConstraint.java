package ru.mephi.bakinaa.lab3.db.constrints;

import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.db.Row;
import ru.mephi.bakinaa.lab3.db.Table;
import ru.mephi.bakinaa.lab3.db.objects.Obj;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.lang.tree.terms.Id;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UniqueConstraint extends Constraint {
    private static final Map<Integer, Obj> EMPTY = Collections.unmodifiableMap(new HashMap<>());
    protected final List<Integer> cols;

    @Override
    public boolean checkOnInsert(Table table, Row row) {
        return check(table, row, EMPTY);
    }

    @Override
    public boolean checkOnModify(Table table, Row row, Map<Integer, Obj> updates) {
        return check(table, row, updates);
    }

    private boolean check(Table table, Row row, Map<Integer, Obj> updates) {
        int expected = hash(row, updates);
        for (var r : table.getRows()) {
            if (r == row)
                continue;
            if (hash(r, EMPTY) == expected && eq(r, row, updates))
                return false;
        }
        return true;
    }

    private int hash(Row row, Map<Integer, Obj> updates) {
        int hash = 0;
        for (int i : cols) {
            Obj val = updates.getOrDefault(i, row.get(i));
            hash ^= Objects.hashCode(val);
        }
        return hash;
    }

    public boolean eq(Row r1, Row r2, Map<Integer, Obj> updates) {
        for (int i : cols) {
            if (!Objects.equals(r1.get(i), updates.getOrDefault(i, r2.get(i))))
                return false;
        }
        return true;
    }
}
