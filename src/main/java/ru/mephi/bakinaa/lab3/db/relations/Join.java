package ru.mephi.bakinaa.lab3.db.relations;

import lombok.Getter;
import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.JoinType;
import ru.mephi.bakinaa.lab3.db.relations.rows.JoinRowView;
import ru.mephi.bakinaa.lab3.db.relations.rows.RowView;
import ru.mephi.bakinaa.lab3.db.relations.rows.SimpleRowMapping;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.utils.FunctionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Join extends AbstractRelation {
    public static final int LEFT_MAPPING_ID = 0;
    public static final int RIGHT_MAPPING_ID = 1;

    private final Relation left;
    private final Relation right;
    private final JoinType type;
    private final Expression condition;

    private List<Integer> leftIndex;
    private List<Integer> rightIndex;
    private int size = 0;

    @Getter
    private SimpleRowMapping mapping;

    public Join(Relation left, Relation right, JoinType type, Expression condition) {
        super(left.getDatabase());
        if (left.getDatabase() != right.getDatabase())
            throw new InvalidDBAccessException("Unable to join tables from different databases");

        this.left = left;
        this.right = right;
        this.type = type;
        this.condition = condition;
        createMapping();
        buildIndexes();
    }

    private void createMapping() {
        mapping = new SimpleRowMapping();
        for (Id id : left.getColumnsSet())
            mapping.register(id, LEFT_MAPPING_ID);
        for (Id id : right.getColumnsSet())
            mapping.register(id, RIGHT_MAPPING_ID);
    }

    private void buildIndexes() {
        size = 0;
        leftIndex = new ArrayList<>();
        rightIndex = new ArrayList<>();

        JoinRowView rowView = new JoinRowView(this, null, null);
        ExpressionContext context = ExpressionContext.create(database, rowView);
        for (int leftIndex = type.leftNullable ? -1 : 0; leftIndex < this.left.getSize(); leftIndex++) {
            for (int rightIndex = type.rightNullable ? -1 : 0; rightIndex < this.right.getSize(); rightIndex++) {
                rowView.setLeft(leftIndex == -1 ? null : this.left.getByIndex(leftIndex));
                rowView.setRight(rightIndex == -1 ? null : this.right.getByIndex(rightIndex));
                if (FunctionUtils.checkPredicate(context, condition)) {
                    this.leftIndex.add(leftIndex);
                    this.rightIndex.add(rightIndex);

                    size++;
                }
            }
        }
    }


    @Override
    public int getSize() {
        return size;
    }

    @Override
    public RowView getByIndex(int index) {
        if (size == 0)
            return null;
        return new JoinRowView(this,
                leftIndex.get(index) == -1 ? null : left.getByIndex(leftIndex.get(index)),
                rightIndex.get(index) == -1 ? null : right.getByIndex(rightIndex.get(index)));    }

    @Override
    public RowView first() {
        if (size == 0)
            return null;
        return new JoinRowView(this,
                leftIndex.getFirst() == -1 ? null : left.getByIndex(leftIndex.getFirst()),
                rightIndex.getFirst() == -1 ? null : right.getByIndex(rightIndex.getFirst()));
    }

    @Override
    public void moveToIndex(RowView view, int index) {
        if (view instanceof JoinRowView rowView) {
            if (rowView.getLeft() != null)
                left.moveToIndex(rowView.getLeft(), leftIndex.get(index));
            else
                rowView.setLeft(left.getByIndex(leftIndex.get(index)));

            if (rowView.getRight() != null)
                right.moveToIndex(rowView.getRight(), rightIndex.get(index));
            else
                rowView.setRight(right.getByIndex(rightIndex.get(index)));
        } else throw new IllegalArgumentException();
    }

    @Override
    public SimpleObj get(int rowId, Id columnId) {
        return mapping.getIncompleteIdIndex(columnId) == LEFT_MAPPING_ID ?
                leftIndex.get(rowId) < 0 ? null : left.get(leftIndex.get(rowId), columnId) :
                rightIndex.get(rowId) < 0 ? null : right.get(rightIndex.get(rowId), columnId);
    }

    @Override
    public Set<Id> getColumnsSet() {
        return mapping.getColumns();
    }

    @Override
    public boolean hasColumn(Id col) {
        return mapping.hasColumns(col);
    }
}
