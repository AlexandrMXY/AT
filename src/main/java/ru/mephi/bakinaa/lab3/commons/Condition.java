package ru.mephi.bakinaa.lab3.commons;

import ru.mephi.bakinaa.lab3.db.views.RowView;
import ru.mephi.bakinaa.lab3.db.views.TablesView;

public interface Condition {
    Condition TRUE_CONDITION = (table, row) -> true;

    boolean check(TablesView table, RowView row);
}
