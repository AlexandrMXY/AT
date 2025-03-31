package ru.mephi.bakinaa.lab3.db.constrints;

import ru.mephi.bakinaa.lab3.db.Row;
import ru.mephi.bakinaa.lab3.db.Table;
import ru.mephi.bakinaa.lab3.db.objects.Obj;

import java.util.Map;

public abstract class Constraint {
    public boolean checkOnInsert(Table table, Row row) { return true; }
    public boolean checkOnRemove(Table table, Row row) { return true; }
    public boolean checkOnModify(Table table, Row row, Map<Integer, Obj> updates) { return true; }
}
