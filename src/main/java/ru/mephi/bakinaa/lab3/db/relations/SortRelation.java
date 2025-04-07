package ru.mephi.bakinaa.lab3.db.relations;

import ru.mephi.bakinaa.lab3.commons.Sort;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.relations.rows.RowView;

import java.util.*;

public class SortRelation extends AbstractRelation {
    private final Relation base;
    private final List<Integer> indexes = new ArrayList<>();

    public SortRelation(Relation base, Sort sort) {
        super(base.getDatabase());
        this.base = base;
        build(sort);
    }

    private void build(Sort sort) {
        int size = base.getSize();
        for (int i = 0; i < size; i++)
            indexes.add(i);

        indexes.sort((a, b) -> {
            SimpleObj left = base.get(a, sort.row());
            SimpleObj right = base.get(b, sort.row());
            if (sort.order() == Sort.Order.ASC)
                return SimpleObj.compare(left, right);
            return -SimpleObj.compare(left, right);
        });
    }

    @Override
    public int getSize() {
        return indexes.size();
    }

    @Override
    public RowView getByIndex(int index) {
        return base.getByIndex(indexes.get(index));
    }

    @Override
    public RowView first() {
        return getByIndex(0);
    }

    @Override
    public void moveToIndex(RowView view, int index) {
        base.moveToIndex(view, indexes.get(index));
    }

    @Override
    public SimpleObj get(int rowId, Id columnId) {
        return base.get(indexes.get(rowId), columnId);
    }

    @Override
    public Set<Id> getColumnsSet() {
        return base.getColumnsSet();
    }

    @Override
    public boolean hasColumn(Id col) {
        return base.hasColumn(col);
    }

}
