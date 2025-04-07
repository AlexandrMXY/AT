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

        if (table.getPKey() == null)
            throw new InvalidDBAccessException("Primary key not specified");
        switch (definition.getIndexType()) {
            case HASHTABLE -> table.setIndex(MapIndex.createHash());
            case TREE, ORDERED -> table.setIndex(MapIndex.createTree());
        }
        tables.put(table.getName(), table);
    }

    private void createModifierConstraint(Table table) {
        for (var col : table.getColumns().getColumnsMap().values()) {
            if (col.isUnique())
                table.addConstraint(new UniqueConstraint(col.getName() + "#unique" , Set.of(col.getIndex())));
            if (!col.isNullable())
                table.addConstraint(new NotNullConstraint(col.getName() + "#notnull", col.getIndex()));
        }
    }

    private void createUserConstraints(Table table, TableDefinition definition) {
        for (var constrDef : definition.getConstraints()) {
            switch (constrDef.getConstraint()) {
                case null -> throw new NullPointerException();
                case UNIQUE -> {
                    Set<Integer> constraintCols = getColumnsIdsFromArg(constrDef.getArgs(), table);
                    table.addConstraint(new UniqueConstraint(constrDef.getId().value, constraintCols));
                }
                case PREDICATE -> {
                    // TODO
                    throw new UnsupportedOperationException();
                }
                case FOREIGN_KEY -> {
                    if (constrDef.getArgs().getArgs().size() != 2)
                        throw new IllegalArgumentException();
                    Id from = (Id) constrDef.getArgs().getArgs().get(0);
                    Id to = (Id) constrDef.getArgs().getArgs().get(1);

                    if (from.scope != null)
                        throw new InvalidDBAccessException("Illegal id");
                    if (to.scope == null)
                        throw new InvalidDBAccessException("Target table not specified");
                    Table targetTable = tables.get(to.scope);
                    if (targetTable == null)
                        throw new InvalidDBAccessException("Unknown table " + to.scope);

                    int fromColumnId = table.getColumns().getIndex(from.value);
                    if (fromColumnId < 0)
                        throw new InvalidDBAccessException("Unknown column " + from.value);
                    int toColumnId = targetTable.getColumns().getIndex(to.value);
                    if (toColumnId < 0)
                        throw new InvalidDBAccessException("Unknown column " + to);

                    Constraint constraint = new ForeignKeyConstraint(constrDef.getId().value, table, fromColumnId, targetTable, toColumnId);
                    table.addConstraint(constraint);
                    targetTable.addConstraint(constraint);
                }
                case PRIMARY_KEY -> {
                    if (table.getPKey() != null)
                        throw new InvalidDBAccessException("Multiple primary keys");

                    Set<Integer> constraintCols = getColumnsIdsFromArg(constrDef.getArgs(), table);
                    table.addConstraint(new PrimaryKeyConstraint(constrDef.getId().value, constraintCols));
                }
            }
        }
    }

    private Set<Integer> getColumnsIdsFromArg(FunArgs args, Table table) {
        Columns columns = table.getColumns();
        Set<Integer> result = new HashSet<>();

        for (var arg : args.getArgs()) {
            if (arg instanceof Id id) {
                if (id.scope != null)
                    throw new InvalidDBAccessException("Illegal id");
                int colIndex = columns.getIndex(id.value);
                if (colIndex < 0)
                    throw new InvalidDBAccessException("Unknown column " + id.value);
                result.add(colIndex);
            } else throw new IllegalArgumentException("Ids expected as args of constraint");
        }

        return result;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("======= Database ").append(name).append(" =======\n");

        tables.forEach((name, table) -> builder.append(table.toString()).append("\n"));

        return builder.toString();
    }
}
