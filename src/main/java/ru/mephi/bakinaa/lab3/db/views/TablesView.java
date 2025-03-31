package ru.mephi.bakinaa.lab3.db.views;

import ru.mephi.bakinaa.lab3.db.Table;
import ru.mephi.bakinaa.lab3.db.views.ColumnView;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.lang.tree.terms.Id;

import java.util.*;

public class TablesView {
    private final Map<String, Map<String, ColumnView>> columns = new HashMap<>();
    private final Map<String, Table> tables = new HashMap<>();


    public ColumnView getColumn(Id id) {
        if (id.scope != null) {
            var tableCols = columns.get(id.scope);
            if (tableCols == null)
                throw new InvalidDBAccessException("Unknown scope " + id.scope);
            return tableCols.get(id.value);
        }
        ColumnView result = null;
        for (var tableCols : columns.values()) {
            ColumnView tableResult = tableCols.get(id.value);
            if (result != null && tableResult != null)
                throw new InvalidDBAccessException("Ambiguous reference to column " + id.value);
            result = tableResult;
        }
        return result;
    }


    public void join(Table table) {
        if (columns.containsKey(table.getName()))
            throw new InvalidDBAccessException("Already joined");
        join0(table, true);
    }

    private void join0(Table table, boolean included) {
        int index = tables.size();
        tables.put(table.getName(), table);
        Map<String, ColumnView> tableColumns = new HashMap<>();
        table.getColumns().getColumnsMap().forEach((name, col) -> {
            tableColumns.put(name, new ColumnView(col, index, included));
        });
        columns.put(table.getName(), tableColumns);
    }

    public void join(TablesView other) {
        for (String table : other.tables.keySet()) {
            if (columns.containsKey(table))
                join0(other.tables.get(table), false);

            var tableColumns = other.columns.get(table);
            var thisColumns = columns.get(table);
            tableColumns.forEach((name, col) -> {
                var thisCol = thisColumns.get(name);
                boolean incValue = col.isIncluded() || thisCol.isIncluded();
                thisCol.setIncluded(incValue);
            });
        }
    }


}
