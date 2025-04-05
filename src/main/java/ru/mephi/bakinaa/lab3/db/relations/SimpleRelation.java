package ru.mephi.bakinaa.lab3.db.relations;

import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.Int;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.JoinType;
import ru.mephi.bakinaa.lab3.db.core.Database;
import ru.mephi.bakinaa.lab3.utils.FunctionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SimpleRelation extends AbstractRelation {
    private final Relation relation;
    private List<Integer> index;

    public SimpleRelation(Relation relation, int skip, int limit, List<Expression> filters) {
        super(relation.getDatabase());
        this.relation = relation;
        buildIndexes(skip, limit, filters);
    }

    private void buildIndexes(int skip, int limit, List<Expression> filters) {
        index = new ArrayList<>();
        int skipped = 0;
        RowView rowView = relation.first();
        ExpressionContext context = ExpressionContext.create(database, rowView);
        for (int i = 0; i < relation.getSize() && index.size() <= limit; i++) {
            relation.moveToIndex(rowView, i);
            if (!checkFilters(context, filters))
                continue;

            if (skipped < skip)
                skipped++;
            else
                index.add(i);
        }
    }

    private boolean checkFilters(ExpressionContext context, List<Expression> filters) {
        for (var filter : filters)
            if (!FunctionUtils.checkPredicate(context, filter))
                return false;
        return true;
    }

    @Override
    public Relation filter(Expression filter) {
        List<Integer> newIndex = new ArrayList<>();
        RowView rowView = relation.first();
        ExpressionContext context = ExpressionContext.create(database, rowView);

        for (int curRowIndex : index) {
            relation.moveToIndex(rowView, curRowIndex);
            if (FunctionUtils.checkPredicate(context, filter))
                newIndex.add(curRowIndex);
        }

        index = newIndex;
        return this;
    }

    @Override
    public Relation limit(int limit) {
        if (limit < 0)
            throw new IllegalArgumentException("Limit should not be less than zero");
        this.index = index.subList(0, Math.min(limit, index.size()));
        return this;
    }

    @Override
    public Relation skip(int skip) {
        if (skip < 0)
            throw new IllegalArgumentException("Skip should not be less than zero");
        if (skip >= index.size())
            index = new ArrayList<>();
        else
            this.index = index.subList(skip, index.size());
        return this;
    }

    @Override
    public int getSize() {
        return index.size();
    }

    @Override
    public RowView getByIndex(int index) {
        return new SimpleRowView(this, index);
    }

    @Override
    public RowView first() {
        return index.isEmpty() ? null : new SimpleRowView(this, 0);
    }

    @Override
    public void moveToIndex(RowView view, int index) {
        if (view instanceof SimpleRowView rowView) {
            rowView.setIndex(this.index.get(index));
        } else throw new IllegalArgumentException();
    }

    @Override
    public SimpleObj get(int rowId, Id columnId) {
        return relation.get(index.get(rowId), columnId);
    }

    @Override
    public Set<Id> getColumnsSet() {
        return relation.getColumnsSet();
    }
}
