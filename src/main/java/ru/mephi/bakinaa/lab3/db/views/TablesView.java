package ru.mephi.bakinaa.lab3.db.views;

import lombok.AllArgsConstructor;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.ResultSet;
import ru.mephi.bakinaa.lab3.db.core.Table;
import ru.mephi.bakinaa.lab3.commons.Condition;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

import java.util.*;

public class TablesView extends Obj {
    private final Map<String, Map<String, ColumnView>> columns = new HashMap<>();
    private final Map<String, Table> tables = new HashMap<>();
    private final Map<Integer, Table> tableIndexesMap = new HashMap<>();

    private int limit = Integer.MAX_VALUE;

    private IJoin root;

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

    public void join(Table table, JoinType type, Condition condition) {
        if (columns.containsKey(table.getName()))
            throw new InvalidDBAccessException("Already joined");

        int index = tables.size();
        tables.put(table.getName(), table);
        tableIndexesMap.put(index, table);
        Map<String, ColumnView> tableColumns = new HashMap<>();
        table.getColumns().getColumnsMap().forEach((name, col) -> {
            tableColumns.put(name, new ColumnView(col, index, true));
        });
        columns.put(table.getName(), tableColumns);

        if (root == null)
            root = new TableView(index, table);
        else
            root = new Join(condition, type, root, new TableView(index, table));
    }

    public RowView next(RowView current) {
        while (root.moveToNext(current)) {
            if (root.checkJoinCondition(current))
                return current;
        }
        return null;
    }

    public RowView first() {
        var view = new RowView(this);
        root.moveToNone(view);
        return next(view);
    }

    public void limit(int limit) {
        if (limit < 0)
            throw new IllegalArgumentException("Limit should not be less than zero");
        this.limit = limit;
    }

    public ResultSet getResult() {
        ResultSet resultSet = new DefaultResultSet();
        RowView rowView = first();
        int count = 0;
        while (rowView != null && count < limit) {
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
            }
            resultSet.addRow(resulRow);
            count++;
            rowView = next(rowView);
        }
        return resultSet;
    }

    private static void setValInList(List<SimpleObj> list, int index, SimpleObj obj) {
        for (int i = list.size(); i <= index; i++)
            list.add(null);
        list.set(index, obj);
    }

    private static abstract class IJoin {
        protected boolean nullable = false;

        abstract boolean hasNext(RowView view);
        abstract void moveToFirst(RowView view);
        abstract void moveToNone(RowView view);
        abstract boolean moveToNext(RowView view);
        abstract boolean inInNonePos(RowView view);
        protected abstract Boolean checkJoinCondition0(RowView view);

        boolean checkJoinCondition(RowView view) {
            Boolean check0 = checkJoinCondition0(view);
            return check0 == null ? nullable : check0;
        }
    }

    @AllArgsConstructor
    private static class TableView extends IJoin {
        int tableIndex;
        private Table table;

        @Override
        public void moveToFirst(RowView view) {
            view.setIndex(tableIndex, 0);
        }

        @Override
        public boolean moveToNext(RowView view) {
            if (hasNext(view)) {
                view.incIndex(tableIndex);
                return true;
            }
            return false;
        }

        @Override
        public void moveToNone(RowView view) {
            view.setIndex(tableIndex, -1);
        }

        @Override
        public boolean hasNext(RowView view) {
            int index = view.getIndex(tableIndex);
            return index + 1 < table.getRowsCnt();
        }

        @Override
        public boolean inInNonePos(RowView view) {
            return view.getIndex(tableIndex) < 0;
        }

        @Override
        public Boolean checkJoinCondition0(RowView view) {
            if (inInNonePos(view))
                return null;
            return true;
        }
    }

    private class Join extends IJoin {
        private final Condition condition;
        private final JoinType type;
        private final IJoin left;
        private final IJoin right;

        public Join(Condition condition, JoinType type, IJoin left, IJoin right) {
            this.condition = condition;
            this.type = type;
            this.left = left;
            this.right = right;

            right.nullable = type.rightNullable;
            left.nullable = type.leftNullable;
        }

        @Override
        public boolean hasNext(RowView view) {
            return left.hasNext(view) || right.hasNext(view);
        }

        @Override
        public void moveToFirst(RowView view) {
            left.moveToFirst(view);
            right.moveToFirst(view);
        }

        @Override
        public void moveToNone(RowView view) {
            left.moveToNone(view);
            right.moveToNone(view);
        }

        @Override
        public boolean inInNonePos(RowView view) {
            return left.inInNonePos(view) && right.inInNonePos(view);
        }

        @Override
        public boolean moveToNext(RowView view) {
            if (right.moveToNext(view))
                return true;
            boolean canMove = left.moveToNext(view);
            if (canMove)
                right.moveToNone(view);
            return canMove;
        }


        @Override
        public Boolean checkJoinCondition0(RowView view) {
            if (inInNonePos(view))
                return null;

            Boolean leftCondition  = left.checkJoinCondition0(view);
            if (leftCondition == null)
                return type.leftNullable;
            Boolean rightCondition = right.checkJoinCondition0(view);
            if (rightCondition == null)
                return type.rightNullable;

            return rightCondition && leftCondition && condition.check(TablesView.this, view);
        }
    }
}
