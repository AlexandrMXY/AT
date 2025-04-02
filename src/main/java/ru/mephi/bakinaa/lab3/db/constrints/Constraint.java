package ru.mephi.bakinaa.lab3.db.constrints;

import ru.mephi.bakinaa.lab3.db.core.Row;
import ru.mephi.bakinaa.lab3.db.core.Table;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;

import java.util.Map;

public abstract class Constraint {
    public boolean checkOnInsert(Table table, Row row) { return true; }
    public boolean checkOnRemove(Table table, Row row) { return true; }
    public boolean checkOnModify(Table table, Row row, Map<Integer, SimpleObj> updates) { return true; }
}
