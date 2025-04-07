package ru.mephi.bakinaa.lab3.db.constrints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.db.relations.rows.Row;
import ru.mephi.bakinaa.lab3.db.relations.Table;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public abstract class Constraint {
    private final String name;

    public boolean checkOnInsert(Table table, Row row) { return true; }
    public boolean checkOnRemove(Table table, Row row) { return true; }
    public boolean checkOnModify(Table table, Row row, Map<Integer, SimpleObj> updates) { return true; }
    public boolean checkOnColRemove(Table table, int colIndex) { return true; }
    public boolean checkOnTableRemove(Table table) { return true; }
}
