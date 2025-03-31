package ru.mephi.bakinaa.lab3.db;

import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

import java.util.HashMap;
import java.util.Map;

public class Columns {
    private Map<String, Column> columns = new HashMap<>();

    public int registerColumn(Column column) {
        if (columns.containsKey(column.getName()))
            throw new InvalidDBAccessException("Column with given name already exists");

        int index = columns.values()
                .stream()
                .map(Column::getIndex)
                .max(Integer::compareTo)
                .orElse(-1) + 1;
        column.setIndex(index);

        columns.put(column.getName(), column);

        return index;
    }

    public int getIndex(String columnName) {
        return columns.get(columnName).getIndex();
    }

    public Column getColumn(String columnName) {
        return columns.get(columnName);
    }

    public Map<String, Column> getColumnsMap() {
        return columns;
    }
}
