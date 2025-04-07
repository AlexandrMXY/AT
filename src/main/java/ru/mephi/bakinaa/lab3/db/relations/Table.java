package ru.mephi.bakinaa.lab3.db.relations;

import lombok.Getter;
import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.db.constrints.*;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.relations.index.Index;
import ru.mephi.bakinaa.lab3.db.relations.rows.Column;
import ru.mephi.bakinaa.lab3.db.relations.rows.Columns;
import ru.mephi.bakinaa.lab3.db.Database;
import ru.mephi.bakinaa.lab3.db.relations.rows.Row;
import ru.mephi.bakinaa.lab3.db.relations.rows.RowView;
import ru.mephi.bakinaa.lab3.db.relations.rows.SimpleRowView;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.lang.defs.ColDefinition;
import ru.mephi.bakinaa.lab3.lang.defs.RowDefinition;
import ru.mephi.bakinaa.lab3.lang.enums.Modifier;
import ru.mephi.bakinaa.lab3.utils.FunctionUtils;
import ru.mephi.bakinaa.lab3.utils.Tuple;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Table extends AbstractRelation {
    private final String name;
    private final Columns columns;
    private final Map<String, Constraint> constraints = new HashMap<>();

    private final List<Row> rows = new ArrayList<>();

    private Set<Integer> pKey;
    private Map<String, Integer> keyRowToKeyTupleIndex = new HashMap<>();
    private Map<Integer, Integer> rowIndexToKeyIndexMap = new HashMap<>();
    private Index index;

    public Table(Database database, String name) {
        super(database);
        this.name = name;
        columns = new Columns(name);
    }

    public void setPKey(Set<Integer> pKeyCols) {
        if (pKey != null)
            throw new InvalidDBAccessException("Primary key already exists");
        pKey = pKeyCols;
        int keyTupleIndex = 0;
        for (var i : pKeyCols) {
            keyRowToKeyTupleIndex.put(columns.getColumn(i).getName(), keyTupleIndex);
            rowIndexToKeyIndexMap.put(i, keyTupleIndex);
            keyTupleIndex++;
        }
        addConstraint(new PrimaryKeyConstraint("#primary", pKeyCols));
    }

    public void setIndex(Index index) {
        this.index = index;
        for (int i = 0; i < rows.size(); i++)
            index.save(getKeyTuple(rows.get(i)), i);
    }

    public void addConstraint(Constraint constraint) {
        if (constraints.containsKey(constraint.getName()))
            throw new InvalidDBAccessException("Constraint with given name already exists");
        constraints.put(constraint.getName(), constraint);
    }

    public void addColumns(List<ColDefinition> definitions) {
        Set<String> names = new HashSet<>();
        for (var definition : definitions) {
            if (names.contains(definition.getName()) || columns.getColumn(definition.getName()) != null)
                throw new InvalidDBAccessException("Unable to create multiple columns with same name");
            if (!definition.getModifiers().isEmpty() && !rows.isEmpty())
                throw new InvalidDBAccessException("Unable to add column with modifiers to not empty table");
            names.add(definition.getName());
        }
        for (var definition : definitions) {
            Column col = new Column(definition.getName(), definition.getType());
            getColumns().registerColumn(col);

            for (Modifier modifier : definition.getModifiers()) {
                if (modifier == Modifier.NOT_NULL) {
                    col.setNullable(false);
                }
                if (modifier == Modifier.UNIQUE) {
                    col.setUnique(true);
                }
                if (modifier == Modifier.PRIMARY) {
                    if (getPKey() != null)
                        throw new InvalidDBAccessException("Multiple primary keys");
                    setPKey(Set.of(col.getIndex()));
                }
            }
        }
    }

    public void removeColumns(Set<Id> ids) {
        for (var id : ids)
            checkIfCanDeleteColumn(id);
        for (var id : ids)
            columns.removeColumn(id.value);
    }

    private void checkIfCanDeleteColumn(Id id) {
        if (id.scope != null && !id.scope.equals(name))
            throw new InvalidDBAccessException("Invalid column " + id);
        if (!columns.hasColumns(id))
            throw new InvalidDBAccessException("Unknown column " + id);
        int colIndex = columns.getIndex(id.value);
        for (var constraint : constraints.values())
            if (!constraint.checkOnColRemove(this, colIndex))
                throw new InvalidDBAccessException("Unable to remove col: volatiles constraint");
    }

    public boolean canDelete() {
        for (var constraint : constraints.values())
            if (!constraint.checkOnTableRemove(this))
                return false;
        return true;
    }

    public Row findAny(Row expected, Set<Integer> cols) {
        int hash = expected.hash(cols);
        for (Row row : rows) {
            if (row.hash(cols) == hash && row.equals(expected, cols))
                return row;
        }
        return null;
    }

    public boolean existsByCols(Row expected, Set<Integer> cols) {
        return findAny(expected, cols) != null;
    }

    public void insert(Row row) {
        for (Column c : columns.getColumnsMap().values()) {
            if (!c.getType().isInstance(row.get(c.getIndex())))
                throw new InvalidDBAccessException("Illegal type");
        }
        for (Constraint c : constraints.values())
            if (!c.checkOnInsert(this, row))
                throw new InvalidDBAccessException("Unable to insert row: row volatiles constraint " + c.toString());
        rows.add(row);
        if (this.index != null)
            index.save(getKeyTuple(row), rows.size() - 1);
    }

    public void insert(RowDefinition rowDefinition) {
        Row row = new Row();
        ExpressionContext ctx = ExpressionContext.create(database);
        rowDefinition.getAssigns().forEach((id, expr) -> {
            if (id.scope != null && !id.scope.equals(name))
                throw new InvalidDBAccessException("Invalid scope");
            row.set(columns.getColumn(id.value).getIndex(), (SimpleObj) expr.call(ctx));
        });
        insert(row);
    }

    public void remove(int index) {
        Row row = rows.get(index);
        for (var constraint : constraints.values())
            if (!constraint.checkOnRemove(this, row))
                throw new InvalidDBAccessException("Unable to remove row: row volatiles constraint");
        if (this.index != null)
            this.index.delete(getKeyTuple(row));
        rows.remove(index);
    }

    public void removeIf(Expression condition) {
        RowView view = first();
        ExpressionContext context = ExpressionContext.create(database, view);
        for (int i = 0; i < rows.size(); i++) {
            moveToIndex(view, i);
            if (FunctionUtils.checkPredicate(context, condition))
                remove(i--);
        }
    }

    @Override
    public Relation findBy(RowDefinition definition) {
        if (index == null)
            return super.findBy(definition);
        Tuple key = getKeyTuple(definition);
        if (key == null)
            return super.findBy(definition);

        int row = index.findById(key);
        if (row < 0)
            return new SimpleRelation(this, List.of());
        return new SimpleRelation(this, List.of(row));
    }

    public void removeBy(RowDefinition definition) {
        if (index == null) {
            removeIf(FunctionUtils.rowDefinitionAsCondition(database, definition));
            return;
        }
        Tuple key = getKeyTuple(definition);
        if (key == null) {
            removeIf(FunctionUtils.rowDefinitionAsCondition(database, definition));
            return;
        }
        int index = this.index.findById(key);
        if (index < 0)
            return;
        Row row = rows.get(index);
        ExpressionContext context = ExpressionContext.create(database);
        for (var colId : definition.getAssigns().keySet()) {
            if (keyRowToKeyTupleIndex.containsKey(colId.value))
                continue;
            if (!Objects.equals(row.get(columns.getIndex(colId.value)),
                    definition.getAssigns().get(colId.value).call(context)))
                return;
        }
        remove(index);
    }

    private Tuple getKeyTuple(RowDefinition definition) {
        Set<Integer> assignedRows = new HashSet<>();
        definition.getAssigns().forEach((id, val) -> {
            assignedRows.add(columns.getIncompleteIdIndex(id));
        });
        if (!assignedRows.containsAll(pKey))
            return null;

        Tuple key = new Tuple(pKey.size());
        ExpressionContext context = ExpressionContext.create(database);
        definition.getAssigns().forEach((row, expr) -> {
            if (keyRowToKeyTupleIndex.containsKey(row.value))
                key.set(keyRowToKeyTupleIndex.get(row.value), (SimpleObj) expr.call(context));
        });
        return key;
    }

    private Tuple getKeyTuple(Row row) {
        Tuple key = new Tuple(pKey.size());
        rowIndexToKeyIndexMap.forEach((rowIndex, keyIndex) -> {
            key.set(keyIndex, row.get(rowIndex));
        });
        return key;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("==== Table ").append(name).append(" ====\n");
        builder.append("Columns:\n").append(columns);
        builder.append("Constraints:\n");
        for (Constraint constraint : constraints.values()) {
            builder.append("\t");
            switch (constraint) {
                case UniqueConstraint c -> {
                    builder.append(c.getClass().getSimpleName()).append("(");
                    builder.append(c.getCols().stream()
                            .map(i -> columns.getColumn(i).getName())
                            .collect(Collectors.joining(", ")));
                    builder.append(") ").append(constraint.getName()).append("\n");
                }
                case ForeignKeyConstraint c -> {
                    builder .append(c.getFromTable().name).append("::")
                            .append(c.getFromTable().columns.getColumn(c.getFromCol()).getName())
                            .append(" -> ")
                            .append(c.getToTable().name).append("::")
                            .append(c.getToTable().columns.getColumn(c.getToCol()).getName())
                            .append(" ").append(constraint.getName()).append("\n")
                            .append("\n");
                }
                case NotNullConstraint c -> {
                    builder.append("NotNull(").append(columns.getColumn(c.getRowIndex()).getName()).append(") ").append(constraint.getName()).append("\n");
                }
                default ->
                    builder.append(constraint.toString()).append(" ").append(constraint.getName()).append("\n");
            }
        }
        builder.append("Data [").append(rows.size()).append(" rows]:\n");
        for (Row row : rows)
            builder.append("\t").append(row).append("\n");


        return builder.toString();
    }

    @Override
    public int getSize() {
        return rows.size();
    }

    @Override
    public RowView getByIndex(int index) {
        if (getSize() > getSize() || index < 0)
            return null;
        return new SimpleRowView(this, index);
    }

    @Override
    public RowView first() {
        if (getSize() == 0)
            return null;
        return new SimpleRowView(this, 0);
    }

    @Override
    public void moveToIndex(RowView view, int index) {
        if (view instanceof SimpleRowView rowView) {
            rowView.setIndex(index);
        } else throw new IllegalArgumentException();
    }

    @Override
    public SimpleObj get(int rowId, Id columnId) {
        return rows.get(rowId).get(columns.getIncompleteIdIndex(columnId));
    }

    @Override
    public Set<Id> getColumnsSet() {
        return columns.getColumns();
    }

    @Override
    public boolean hasColumn(Id col) {
        return columns.hasColumns(col);
    }

}

