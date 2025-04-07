package ru.mephi.bakinaa.lab3.db.relations;


import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.commons.Sort;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.JoinType;
import ru.mephi.bakinaa.lab3.db.Database;
import ru.mephi.bakinaa.lab3.db.relations.rows.*;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.lang.defs.RowDefinition;

import java.util.*;

public class GroupRelation extends AbstractRelation {
    private RowMapping mapping;

    private final List<Row> rows = new ArrayList<>();

    public GroupRelation(Relation base, Set<Id> grouping, RowDefinition aggregator) {
        super(base.getDatabase());
        build(base, grouping, aggregator);
    }

    private void build(Relation base, Set<Id> cols, RowDefinition aggregators) {
        createMapping(cols, aggregators);

        Map<Row, List<Integer>> groupsMap = new HashMap<>();

        findGroups(base, cols, groupsMap);

        groupsMap.forEach((group, rowsIds) -> {
            Relation groupRelation = new GroupRelation.Group(base, rowsIds);
            ExpressionContext context = ExpressionContext.create(groupRelation);

            aggregators.getAssigns().forEach((column, aggregator) -> {
                group.set(
                        mapping.getIncompleteIdIndex(column),
                        (SimpleObj) aggregator.call(context));
            });
            rows.add(group);
        });
    }

    private void findGroups(Relation base, Set<Id> cols, Map<Row, List<Integer>> groupsMap) {
        for (int i = 0; i < base.getSize(); i++) {
            Row row = new Row();
            for (var col : cols)
                row.set(mapping.getIncompleteIdIndex(col), base.get(i, col));
            int index = i;
            groupsMap.compute(row, (r, idsList) -> {
                if (idsList == null)
                    idsList = new ArrayList<>();
                idsList.add(index);
                return idsList;
            });
        }
    }

    private void createMapping(Set<Id> cols, RowDefinition aggregator) {
        SimpleRowMapping mapping = new SimpleRowMapping();
        int colIndex = 0;
        for (var colId : cols)
            mapping.register(colId, colIndex++);
        for (var colId : aggregator.getAssigns().keySet()) {
            if (colId.scope != null)
                throw new InvalidDBAccessException("Invalid col id");
            mapping.register(colId, colIndex++);
        }
        this.mapping = mapping;
    }


    @Override
    public int getSize() {
        return rows.size();
    }

    @Override
    public RowView getByIndex(int index) {
        if (index < 0 || index >= getSize())
            return null;
        return new SimpleRowView(this, index);
    }

    @Override
    public RowView first() {
        if (getSize() == 0)
            return null;
        return new SimpleRowView(this, 0);
    }

    @Override
    public void moveToIndex(RowView view, int index) {
        if (view instanceof SimpleRowView rowView) {
            ((SimpleRowView) view).setIndex(index);
        } else throw new IllegalArgumentException();
    }

    @Override
    public SimpleObj get(int rowId, Id columnId) {
        return rows.get(rowId).get(mapping.getIncompleteIdIndex(columnId));
    }

    @Override
    public Set<Id> getColumnsSet() {
        return mapping.getColumns();
    }

    @Override
    public boolean hasColumn(Id col) {
        return mapping.hasColumns(col);
    }

    private static class Group extends AbstractRelation {
        private final Relation base;
        private final List<Integer> ids;

        public Group(Relation base, List<Integer> ids) {
            super(base.getDatabase());
            this.base = base;
            this.ids = ids;
        }

        @Override
        public int getSize() {
            return ids.size();
        }

        @Override
        public RowView getByIndex(int index) {
            if (index < 0 || index >= getSize())
                return null;
            return new SimpleRowView(this, index);
        }

        @Override
        public RowView first() {
            return getSize() == 0 ? null : new SimpleRowView(this, 0);
        }


        @Override
        public void moveToIndex(RowView view, int index) {
            if (view instanceof SimpleRowView rowView) {
                rowView.setIndex(index);
            } else throw new IllegalArgumentException();
        }

        @Override
        public SimpleObj get(int rowId, Id columnId) {
            return base.get(ids.get(rowId), columnId);
        }

        @Override
        public Set<Id> getColumnsSet() {
            return base.getColumnsSet();
        }

        @Override
        public Database getDatabase() {
            return base.getDatabase();
        }


        @Override
        public Relation limit(int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Relation skip(int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Relation project(Set<Id> cols) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Relation filter(Expression filter) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Relation join(Relation other, JoinType type, Expression condition) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Relation group(Set<Id> columns, RowDefinition aggregator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Relation map(RowDefinition mapper) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Relation sort(Sort sort) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasColumn(Id col) {
            return base.hasColumn(col);
        }
    }
}
