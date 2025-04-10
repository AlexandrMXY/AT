package ru.mephi.bakinaa.lab3.db.constrints;

import lombok.ToString;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.relations.Table;
import ru.mephi.bakinaa.lab3.db.relations.rows.Row;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ToString
public class ForeignConstraint extends Constraint {
    @ToString.Exclude
    private final Table from;
    @ToString.Exclude
    private final Table to;
    private final Map<Integer, Integer> mapping;
    @ToString.Exclude
    private final PrimaryKeyConstraint targetKeyConstraint;
    private final Set<Integer> targetColsSet;
    private final Set<Integer> sourcesColsSet;

    public ForeignConstraint(String name, Table from, Table to, Map<Integer, Integer> mapping) {
        super(name);
        this.from = from;
        this.to = to;
        this.mapping = mapping;
        this.targetKeyConstraint = to.getPKeyConstraint();
        targetColsSet = new HashSet<>(mapping.values());
        sourcesColsSet = mapping.keySet();

        if (!targetColsSet.equals(to.getPKey()))
            throw new InvalidDBAccessException("Invalid mapping");

        targetKeyConstraint.addReference(this);
    }

    @Override
    public boolean checkOnInsert(Table table, Row row) {
        if (table == from) {
            Row target = new Row();
            mapping.forEach((fromId, toId) -> {
                target.set(toId, row.get(fromId));
            });
            return to.existsByCols(target, targetColsSet);
        }
        return true;
    }

    @Override
    public boolean checkOnRemove(Table table, Row row) {
        if (table == to) {
            Row source = new Row();
            mapping.forEach((fromId, toId) -> {
                source.set(fromId, row.get(toId));
            });
            return !from.existsByCols(source, sourcesColsSet);
        }
        return true;
    }

    @Override
    public boolean checkOnModify(Table table, Row row, Map<Integer, SimpleObj> updates) {
        if (table == from) {
            Row target = new Row();
            mapping.forEach((fromId, toId) -> {
                target.set(toId, updates.getOrDefault(fromId, row.get(fromId)));
            });
            return to.existsByCols(target, targetColsSet);
        } else if (table == to) {
            Row source = new Row();
            mapping.forEach((fromId, toId) -> {
                source.set(fromId, updates.getOrDefault(toId, row.get(toId)));
            });
            return !from.existsByCols(source, sourcesColsSet);
        }
        throw new InvalidDBAccessException("Illegal table");
    }

    @Override
    public void remove() {
        targetKeyConstraint.removeReference(this);
        //to.forceRemoveConstraint(getName());
        from.forceRemoveConstraint(getName());
    }
}
