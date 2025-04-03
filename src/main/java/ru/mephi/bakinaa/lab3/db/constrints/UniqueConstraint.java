package ru.mephi.bakinaa.lab3.db.constrints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.db.core.Row;
import ru.mephi.bakinaa.lab3.db.core.Table;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class UniqueConstraint extends Constraint {
    protected static final Map<Integer, SimpleObj> EMPTY = Collections.unmodifiableMap(new HashMap<>());
    protected final List<Integer> cols;

    @Override
    public boolean checkOnInsert(Table table, Row row) {
        return check(table, row, EMPTY);
    }

    @Override
    public boolean checkOnModify(Table table, Row row, Map<Integer, SimpleObj> updates) {
        return check(table, row, updates);
    }

    private boolean check(Table table, Row row, Map<Integer, SimpleObj> updates) {
        int expected = hash(row, updates);
        for (var r : table.getRows()) {
            if (r == row)
                continue;
            if (hash(r, EMPTY) == expected && eq(r, row, updates))
                return false;
        }
        return true;
    }

    private int hash(Row row, Map<Integer, SimpleObj> updates) {
        int hash = 0;
        for (int i : cols) {
            SimpleObj val = updates.getOrDefault(i, row.get(i));
            hash ^= Objects.hashCode(val);
        }
        return hash;
    }

    public boolean eq(Row r1, Row r2, Map<Integer, SimpleObj> updates) {
        for (int i : cols) {
            if (!Objects.equals(r1.get(i), updates.getOrDefault(i, r2.get(i))))
                return false;
        }
        return true;
    }
}
