package ru.mephi.bakinaa.lab3.db.relations.rows;

import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class Columns implements RowMapping {
    private final String tableName;
    private final Map<String, Column> columns = new HashMap<>();
    private final Map<Integer, Column> columnIndexesMap = new HashMap<>();
    private final Set<Id> columnsSet = new HashSet<>();

    public void removeColumn(String col) {
        Column column = columns.get(col);
        columns.remove(col);
        columnIndexesMap.remove(column.getIndex());
        columnsSet.remove(new Id(tableName, column.getName()));
    }

    public void renameColumn(String originalName, String newName) {
        Column column = columns.get(originalName);
        if (column == null)
            throw new InvalidDBAccessException("Unknown column " + originalName);
        if (originalName.equals(newName))
            return;
        if (columns.containsKey(newName))
            throw new InvalidDBAccessException("Column with name " + newName + " already exists");

        columnsSet.remove(new Id(tableName, newName));
        columns.put(newName, column);
        columnsSet.remove(new Id(tableName, originalName));
        columns.remove(originalName);
        column.setName(newName);
    }

    public void registerColumn(Column column) {
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
        columnsSet.add(new Id(tableName, column.getName()));
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
            builder.append("\t").append(col.getType()).append(" ");
            builder.append(name).append(" ").append(col.getIndex());
            if (!col.isNullable())
                builder.append(" notnull");
            if (col.isUnique())
                builder.append(" unique");
            builder.append("\n");
        });

        return builder.toString();
    }

    public Column getColumn(int index) {
        return columnIndexesMap.get(index);
    }

    @Override
    public int getIncompleteIdIndex(Id id) {
        if (id.scope != null && !id.scope.equals(tableName))
            throw new InvalidDBAccessException("Unknown table column " + id);
        if (!columns.containsKey(id.value))
            throw new InvalidDBAccessException("Unknown table column " + id);
        return columns.get(id.value).getIndex();
    }

    @Override
    public Set<Id> getColumns() {
        return columnsSet;
    }

    @Override
    public boolean hasColumns(Id id) {
        return (id.scope == null || id.scope.equals(tableName)) && columns.containsKey(id.value);
    }
}
