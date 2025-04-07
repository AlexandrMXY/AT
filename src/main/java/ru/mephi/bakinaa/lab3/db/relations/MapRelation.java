package ru.mephi.bakinaa.lab3.db.relations;

import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.relations.rows.Row;
import ru.mephi.bakinaa.lab3.db.relations.rows.RowView;
import ru.mephi.bakinaa.lab3.db.relations.rows.SimpleRowMapping;
import ru.mephi.bakinaa.lab3.db.relations.rows.SimpleRowView;
import ru.mephi.bakinaa.lab3.lang.defs.RowDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MapRelation extends AbstractRelation {
    private final SimpleRowMapping mapping = new SimpleRowMapping();
    private final List<Row> rows = new ArrayList<>();

    public MapRelation(Relation base, RowDefinition mapper) {
        super(base.getDatabase());
        build(base, mapper);
    }

    private void build(Relation relation, RowDefinition mapper) {
        int indexCounter = 0;
        for (var id : mapper.getAssigns().keySet())
            mapping.register(id, indexCounter++);

        RowView rowView = relation.first();
        ExpressionContext context = ExpressionContext.create(database, rowView);
        for (int i = 0; i < relation.getSize(); i++) {
            relation.moveToIndex(rowView, i);
            Row row = new Row();
            mapper.getAssigns().forEach((col, valFun) -> {
                row.set(mapping.getIncompleteIdIndex(col), (SimpleObj) valFun.call(context));
            });
            rows.add(row);
        }
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
            rowView.setIndex(index);
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

}
