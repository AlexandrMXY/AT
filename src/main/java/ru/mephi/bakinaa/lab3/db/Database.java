package ru.mephi.bakinaa.lab3.db;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.db.constrints.*;
import ru.mephi.bakinaa.lab3.db.relations.Table;
import ru.mephi.bakinaa.lab3.db.relations.index.MapIndex;
import ru.mephi.bakinaa.lab3.db.relations.rows.Column;
import ru.mephi.bakinaa.lab3.db.relations.rows.Columns;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.lang.FunArgs;
import ru.mephi.bakinaa.lab3.lang.defs.ConstraintDefinition;
import ru.mephi.bakinaa.lab3.lang.enums.IndexType;
import ru.mephi.bakinaa.lab3.lang.enums.Modifier;
import ru.mephi.bakinaa.lab3.lang.defs.TableDefinition;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

import java.util.*;

@RequiredArgsConstructor
@Getter
public class Database {
    private final String name;
    private final Map<String, Table> tables = new HashMap<>();

    public Table getTable(String name) {
        return tables.get(name);
    }

    public Table getTable(Id id) {
        if (id.scope != null)
            throw new InvalidDBAccessException("Illegal id");
        return getTable(id.value);
    }

    public void deleteTable(Id id) {
        if (id.scope != null)
            throw new InvalidDBAccessException("Invalid id " + id);
        Table table = tables.get(id.value);
        if (table == null)
            throw new InvalidDBAccessException("Unknown table " + id);
        if (!table.canDelete())
            throw new InvalidDBAccessException("Table deletion volatiles constraints");
        tables.remove(id.value);
    }

    public void createTable(TableDefinition definition) {
        Table table = new Table(this, definition.getId().value);

        table.addColumns(definition.getCols());

        createUserConstraints(table, definition);
        createModifierConstraint(table);

        if (table.getPKey() == null && definition.getIndexType() != IndexType.NONE)
            throw new InvalidDBAccessException("Primary key not specified. Unable to create indexed table");
        switch (definition.getIndexType()) {
            case HASHTABLE -> table.setIndex(MapIndex.createHash());
            case TREE, ORDERED -> table.setIndex(MapIndex.createTree());
        }
        tables.put(table.getName(), table);
    }

    private void createModifierConstraint(Table table) {
        for (var col : table.getColumns().getColumnsMap().values()) {
            if (col.isUnique())
                table.addConstraint(new UniqueConstraint(col.getName() + "#unique", table , Set.of(col.getIndex())));
            if (!col.isNullable())
                table.addConstraint(new NotNullConstraint(col.getName() + "#notnull", table, col.getIndex()));
        }
    }

    private void createUserConstraints(Table table, TableDefinition definition) {
        for (var constrDef : definition.getConstraints()) {
            table.addConstraint(constrDef);
        }
    }



    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("======= Database ").append(name).append(" =======\n");

        tables.forEach((name, table) -> builder.append(table.toString()).append("\n"));

        return builder.toString();
    }
}
