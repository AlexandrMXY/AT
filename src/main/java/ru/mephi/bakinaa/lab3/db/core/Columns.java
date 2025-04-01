package ru.mephi.bakinaa.lab3.db.core;

import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

import java.util.HashMap;
import java.util.Map;

public class Columns {
    private Map<String, Column> columns = new HashMap<>();
    private Map<Integer, Column> columnIndexesMap = new HashMap<>();

    public int registerColumn(Column column) {
        if (columns.containsKey(column.getName()))
            throw new InvalidDBAccessException("Column with name " + column.getName() + " already exists");

        int index = columns.values()
                .stream()
                .map(Column::getIndex)
                .max(Integer::compareTo)
                .orElse(-1) + 1;
        column.setIndex(index);

        columns.put(column.getName(), column);
        columnIndexesMap.put(index, column);

        return index;
    }

    public int getIndex(String columnName) {
        if (columns.containsKey(columnName))
            return columns.get(columnName).getIndex();
        return -1;
    }

    public Column getColumn(String columnName) {
        return columns.get(columnName);
    }

    public Map<String, Column> getColumnsMap() {
        return columns;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        columns.forEach((name, col) -> {
            builder.append(name).append(" ").append(col.getIndex());
            if (!col.isNullable())
                builder.append(" notnull");
            if (col.isPrimary())
                builder.append(" primary");
            if (col.isUnique())
                builder.append(" unique");
            builder.append("\n");
        });

        return builder.toString();
    }

    public Column getColumn(int index) {
        return columnIndexesMap.get(index);
    }
}
