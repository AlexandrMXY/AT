package ru.mephi.bakinaa.lab3.util;

import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.relations.Relation;
import ru.mephi.bakinaa.lab3.db.relations.rows.RowView;

import java.util.Comparator;

@RequiredArgsConstructor
public class RelationRowComparator implements Comparator<RowView> {
    private final Relation relation;

    @Override
    public int compare(RowView left, RowView right) {
        for (var key : relation.getColumnsSet()) {
            int cmp = SimpleObj.compare(left.get(key), right.get(key));
            if (cmp != 0)
                return cmp;
        }
        return 0;
    }
}
