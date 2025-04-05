package ru.mephi.bakinaa.lab3.db.views__;

import lombok.Getter;
import ru.mephi.bakinaa.lab3.commons.*;
import ru.mephi.bakinaa.lab3.commons.objects.Bool;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.JoinType;
import ru.mephi.bakinaa.lab3.db.ResultSet;
import ru.mephi.bakinaa.lab3.db.core.Database;
import ru.mephi.bakinaa.lab3.db.core.Table;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

import java.util.*;

public class TablesView extends Obj {
    @Getter
    private Database database;
    private final Map<String, Map<String, ColumnView>> columns = new HashMap<>();
    private final Map<String, Table> tables = new HashMap<>();
    private final Map<Integer, Table> tableIndexesMap = new HashMap<>();

    private long limit = Long.MAX_VALUE;
    private long skip = 0L;
    private final List<Expression> filters = new ArrayList<>();

    private Join root;

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
    public Table getTable(int tableIndex) {
        return tableIndexesMap.get(tableIndex);
    }
    public int getTablesCnt() {
        return tables.size();
    }

    public void join(Table table, JoinType type, Expression condition) {
        if (this.database != null && table.getDatabase() != database)
            throw new InvalidDBAccessException("Unable to join tables from different databases");
        if (columns.containsKey(table.getName()))
            throw new InvalidDBAccessException("Already joined");

        database = table.getDatabase();

        int index = tables.size();
        tables.put(table.getName(), table);
        tableIndexesMap.put(index, table);
        Map<String, ColumnView> tableColumns = new HashMap<>();
        table.getColumns().getColumnsMap().forEach((name, col) -> {
            tableColumns.put(name, new ColumnView(col, index, true));
        });
        columns.put(table.getName(), tableColumns);

        if (root == null)
            root = new SingleTableJoin(index, table);
        else
            root = new JoinImpl(this, condition, type, root, new SingleTableJoin(index, table));
    }

    public void addFilter(Expression filter) {
        filters.add(filter);
    }

    private RowView next(RowView current) {
        while (root.moveToNext(current)) {
            if (root.checkJoinCondition(current) && checkFilters(current))
                return current;
        }
        return null;
    }

    private RowView first() {
        var view = new RowView(this);
        root.moveToNone(view);
        return next(view);
    }

    public void limit(long limit) {
        if (limit < 0)
            throw new IllegalArgumentException("Limit should not be less than zero");
        this.limit = limit;
    }


    public void skip(long skip) {
        if (skip < 0)
            throw new IllegalArgumentException("Skip should not be less than zero");
        this.skip = skip;
    }

    public ResultSet getResult() {
        ResultSet resultSet = new DefaultResultSet();
        RowView rowView = first();
        long count = 0;
        while (rowView != null && count < limit) {
            addToResultSet(resultSet, rowView);
            count++;
            rowView = next(rowView);
        }
        return resultSet;
    }

    private boolean checkFilters(RowView rowView) {
        ExpressionContext context = ExpressionContext.create(database, rowView);
        for (var filter : filters) {
            Obj res = filter.call(context);
            if (res == null)
                return false;
            if (res instanceof Bool bool) {
                if (!bool.value)
                    return false;
            } else throw new InvalidDBAccessException("Filter should return boolean value");
        }
        return true;
    }

    private void addToResultSet(ResultSet resultSet, RowView rowView) {
        List<SimpleObj> resulRow = new ArrayList<>();
        for (int tableIndex = 0; tableIndex < rowView.getRows().size(); tableIndex++) {
            int indexInTable = rowView.getRows().get(tableIndex);
            if (indexInTable < 0)
                continue;

            Table table = tableIndexesMap.get(tableIndex);
            table.getColumns().getColumnsMap().values().forEach((column) -> {
                setValInList(
                        resulRow,
                        resultSet.getOrCreateIndex(
                                new Id(
                                        table.getName(),
                                        column.getName())),
                        table.getRow(indexInTable).get(column.getIndex()));
            });

            resultSet.addRow(resulRow);
        }
    }

    private static void setValInList(List<SimpleObj> list, int index, SimpleObj obj) {
        for (int i = list.size(); i <= index; i++)
            list.add(null);
        list.set(index, obj);
    }

}
