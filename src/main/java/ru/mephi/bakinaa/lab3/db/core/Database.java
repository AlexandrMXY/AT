package ru.mephi.bakinaa.lab3.db.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.db.constrints.*;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.lang.enums.Modifier;
import ru.mephi.bakinaa.lab3.lang.tree.ExprSet;
import ru.mephi.bakinaa.lab3.lang.tree.defs.TableDefinition;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class Database {
    private final String name;
    private final Map<String, Table> tables = new HashMap<>();

    public Table getTable(String name) {
        return tables.get(name);
    }

    public void createTable(TableDefinition definition) {
        Table table = new Table(this, definition.getId().value);

        for (var colDef : definition.getCols()) {
            Column col = new Column(colDef.getName());
            table.getColumns().registerColumn(col);

            for (Modifier modifier : colDef.getModifiers()) {
                if (modifier == Modifier.NOT_NULL) {
                    col.setNullable(false);
                }
                if (modifier == Modifier.UNIQUE) {
                    col.setUnique(true);
                }
                if (modifier == Modifier.PRIMARY) {
                    if (table.getPKey() != null)
                        throw new InvalidDBAccessException("Multiple primary keys");
                    table.setPKey(List.of(col.getIndex()));
                }
            }
        }
        createUserConstraints(table, definition);
        createModifierConstraint(table);

        if (table.getPKey() == null)
            throw new InvalidDBAccessException("Primary key not specified");
        tables.put(table.getName(), table);
    }

    private void createModifierConstraint(Table table) {
        for (var col : table.getColumns().getColumnsMap().values()) {
            if (col.isUnique())
                table.addConstraint(new UniqueConstraint(List.of(col.getIndex())));
            if (!col.isNullable())
                table.addConstraint(new NotNullConstraint(col.getIndex()));
        }
    }

    private void createUserConstraints(Table table, TableDefinition definition) {
        for (var constrDef : definition.getConstraints()) {
            switch (constrDef.getConstraint()) {
                case null -> throw new NullPointerException();
                case UNIQUE -> {
                    List<Integer> constraintCols = getColumnsIdsFromExprArg(constrDef.getArgs(), table);
                    table.addConstraint(new UniqueConstraint(constraintCols));
                }
                case PREDICATE -> {
                    // TODO
                    throw new UnsupportedOperationException();
                }
                case FOREIGN_KEY -> {
                    if (constrDef.getArgs().getExprs().size() != 2)
                        throw new IllegalArgumentException();
                    Id from = (Id)constrDef.getArgs().getExprs().get(0);
                    Id to = (Id)constrDef.getArgs().getExprs().get(1);

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

                    Constraint constraint = new ForeignKeyConstraint(table, fromColumnId, targetTable, toColumnId);
                    table.addConstraint(constraint);
                    targetTable.addConstraint(constraint);
                }
                case PRIMARY_KEY -> {
                    if (table.getPKey() != null)
                        throw new InvalidDBAccessException("Multiple primary keys");

                    List<Integer> constraintCols = getColumnsIdsFromExprArg(constrDef.getArgs(), table);
                    table.addConstraint(new PrimaryKeyConstraint(constraintCols));
                }
            }
        }
    }

    private List<Integer> getColumnsIdsFromExprArg(ExprSet exprSet, Table table) {
        Columns columns = table.getColumns();
        List<Integer> result = new ArrayList<>();

        for (var node : exprSet.getExprs()) {
            if (node instanceof Id id) {
                if (id.scope != null)
                    throw new InvalidDBAccessException("Illegal id");
                int colIndex = columns.getIndex(id.value);
                if (colIndex < 0)
                    throw new InvalidDBAccessException("Unknown column " + id.value);
                result.add(colIndex);
            } else throw new IllegalArgumentException("Ids expected as args");
        }

        return result;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("======= Database ").append(name).append(" =======\n");

        tables.forEach((name, table) -> builder.append(table.toString()));

        return builder.toString();
    }
}
