package ru.mephi.bakinaa.lab3.db.relations.rows;

import ru.mephi.bakinaa.lab3.commons.objects.Id;

import java.util.List;
import java.util.Set;

public interface RowMapping {
    int getIncompleteIdIndex(Id id);
    Set<Id> getColumns();
    boolean hasColumns(Id id);
}
