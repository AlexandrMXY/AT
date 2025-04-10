package ru.mephi.bakinaa.lab3.db.relations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.commons.Sort;
import ru.mephi.bakinaa.lab3.commons.objects.Bool;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.Int;
import ru.mephi.bakinaa.lab3.db.JoinType;
import ru.mephi.bakinaa.lab3.db.Database;
import ru.mephi.bakinaa.lab3.db.relations.rows.RowView;
import ru.mephi.bakinaa.lab3.lang.defs.RowDefinition;
import ru.mephi.bakinaa.lab3.utils.FunctionUtils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Getter
public abstract class AbstractRelation implements Relation, Serializable {
    @JsonIgnore
    protected final Database database;

    protected AbstractRelation(Database database) {
        this.database = database;
    }

    @Override
    public Relation sort(Sort sort) {
        return new SortRelation(this, sort);
    }

    @Override
    public Obj reduce(Expression initial, Expression reducer) {
        ExpressionContext context = ExpressionContext.create();

        Obj value = initial == null ? null : initial.call(context);
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
    public Relation findBy(RowDefinition definition) {
        return filter(FunctionUtils.rowDefinitionAsCondition(database, definition));
    }

    @Override
    public Relation project(Set<Id> cols) {
        return new ProjectRelation(this, cols);
    }

    @Override
    public Relation group(Set<Id> columns, RowDefinition aggregator) {
        return new GroupRelation(this, columns, aggregator);
    }

    @Override
    public Relation map(RowDefinition mapper) {
        return new MapRelation(this, mapper);
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

    @Override
    public Iterator<RowView> iterator() {
        return new Iter();
    }

    private class Iter implements Iterator<RowView> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < getSize();
        }

        @Override
        public RowView next() {
            return getByIndex(i++);
        }
    }
}
