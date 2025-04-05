package ru.mephi.bakinaa.lab3.db.core;

import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Row {
    private final List<SimpleObj> data = new ArrayList<>();

    public SimpleObj get(int index) {
        if (index < 0)
            throw new InvalidDBAccessException();
        if (index >= data.size())
            return null;
        return data.get(index);
    }

    public void set(int index, SimpleObj val) {
        if (index < 0)
            throw new InvalidDBAccessException();

        for (int i = data.size(); i <= index; i++)
            data.add(null);

        data.set(index, val);
    }

    public int hash(List<Integer> cols) {
        return cols.stream().map(i -> Objects.hashCode(get(i))).reduce(0, (a, b) -> a ^ b);
    }

    public boolean equals(Row other, List<Integer> cols) {
        for (int i : cols)
            if (!Objects.equals(this.get(i), other.get(i)))
                return false;
        return true;
    }

    public static Row withCol(int colId, SimpleObj val) {
        Row res = new Row();
        res.set(colId, val);
        return res;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (var obj : data)
            builder.append(obj).append(" ");

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        int len = Math.max(data.size(), row.data.size());
        for (int i = 0; i < len; i++) {
            if (!Objects.equals(get(i), row.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        int offset = 0;
        for (SimpleObj obj : data) {
            offset = (offset + 1) % 32;
            if (obj != null) {
                int objHash = obj.hashCode();
                // xor with circular bitshift of hash
                hash ^= (objHash >>> objHash) | (objHash << (32 - offset));
            }
        }
        return hash;
    }
}
