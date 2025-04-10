package ru.mephi.bakinaa.lab3.db.relations;

import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.relations.rows.RowView;
import ru.mephi.bakinaa.lab3.db.relations.rows.SimpleRowView;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

import java.util.HashSet;
import java.util.Set;

// TODO исправить некоректоне  несуществующих столбцов / столбцов с неполным id
public class ProjectRelation extends AbstractRelation {
    private final Set<Id> cols;
    private final Set<String> inaccurateCols;
    private final Relation base;

    public ProjectRelation(Relation base, Set<Id> cols) {
        super(base.getDatabase());
        this.base = base;
        this.cols = new HashSet<>();
        this.inaccurateCols = new HashSet<>();
        for (var col : cols) {
            if (!base.hasColumn(col))
                throw new InvalidDBAccessException("Unknown colum " + col);
            this.cols.add(col);
            if (inaccurateCols.contains(col.value))
                inaccurateCols.remove(col.value);
            else
                inaccurateCols.add(col.value);
        }
    }

    @Override
    public int getSize() {
        return base.getSize();
    }

    @Override
    public RowView getByIndex(int index) {
        if (index < 0 || index >= getSize())
            return null;
        return new SimpleRowView(this, index);
    }

    @Override
    public RowView first() {
        return getByIndex(0);
    }

    @Override
    public void moveToIndex(RowView view, int index) {
        if (view instanceof SimpleRowView rowView) {
            rowView.setIndex(index);
        } else throw new IllegalArgumentException();
    }

    @Override
    public SimpleObj get(int rowId, Id columnId) {
        if (cols.contains(columnId))
            return base.get(rowId, columnId);
        throw new InvalidDBAccessException("Unknown column " + columnId);
    }

    @Override
    public Set<Id> getColumnsSet() {
        return cols;
    }

    @Override
    public boolean hasColumn(Id col) {
        return col.scope == null ? inaccurateCols.contains(col.value) : cols.contains(col);
    }
}
