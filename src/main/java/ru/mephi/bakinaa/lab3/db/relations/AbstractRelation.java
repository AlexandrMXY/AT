package ru.mephi.bakinaa.lab3.db.relations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.commons.objects.Bool;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.Int;
import ru.mephi.bakinaa.lab3.db.JoinType;
import ru.mephi.bakinaa.lab3.db.core.Database;
import ru.mephi.bakinaa.lab3.utils.FunctionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public abstract class AbstractRelation implements Relation {
    protected final Database database;

    @Override
    public Obj reduce(Expression initial, Expression reducer) {
        ExpressionContext context = ExpressionContext.create();

        Obj value = initial.call(context);
        RowView rowView = first();
        context.setRow(rowView);

        for (int i = 0; i < getSize(); i++) {
            moveToIndex(rowView, i);
            context.setValue(REDUCE_ACCUMULATOR_VARIABLE, value);
            value = reducer.call(context);
        }

        return value;
    }

    @Override
    public Bool isEmpty() {
        return Bool.of(getSize() == 0);
    }

    @Override
    public Int count() {
        return new Int(getSize());
    }

    @Override
    public Bool anyMatch(Expression predicate) {
        RowView rowView = first();
        ExpressionContext context = ExpressionContext.create(database, rowView);
        for (int i = 0; i < getSize(); i++) {
            moveToIndex(rowView, i);
            if (FunctionUtils.checkPredicate(context, predicate))
                return Bool.TRUE;
        }
        return Bool.FALSE;
    }

    @Override
    public Bool allMatch(Expression predicate) {
        RowView rowView = first();
        ExpressionContext context = ExpressionContext.create(database, rowView);
        for (int i = 0; i < getSize(); i++) {
            moveToIndex(rowView, i);
            if (!FunctionUtils.checkPredicate(context, predicate))
                return Bool.FALSE;
        }
        return Bool.TRUE;
    }

    @Override
    public Relation limit(int value) {
        return new SimpleRelation(this, 0, value, List.of());
    }

    @Override
    public Relation skip(int value) {
        return new SimpleRelation(this, value, 0, List.of());
    }

    @Override
    public Relation filter(Expression filter) {
        return new SimpleRelation(this, 0, Integer.MAX_VALUE, List.of(filter));
    }

    @Override
    public Relation join(Relation other, JoinType type, Expression condition) {
        return new Join(this, other, type, condition);
    }

    @Override
    public Relation project(List<Id> cols) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Relation group(List<Id> columns, Expression aggregator) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Relation map(Expression mapper) {
        // TODO
        throw new UnsupportedOperationException();
    }


    @Override
    public String toString() {
        Set<Id> cols = getColumnsSet();
        StringBuilder builder = new StringBuilder();
        for (var col : cols)
            builder.append(col).append(" ");
        builder.append("\n");

        for (int i = 0; i < getSize(); i++) {
            for (var col : cols)
                builder.append(get(i, col)).append(" ");
            builder.append("\n");
        }

        return builder.toString();
    }
}
