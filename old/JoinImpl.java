package ru.mephi.bakinaa.lab3.db.views__;

import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.commons.objects.Bool;
import ru.mephi.bakinaa.lab3.db.JoinType;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

class JoinImpl extends Join {
    private final TablesView tablesView;
    private final Expression condition;
    private final JoinType type;
    private final Join left;
    private final Join right;

    public JoinImpl(TablesView tablesView, Expression condition, JoinType type, Join left, Join right) {
        this.tablesView = tablesView;
        this.condition = condition;
        this.type = type;
        this.left = left;
        this.right = right;

        right.nullable = type.rightNullable;
        left.nullable = type.leftNullable;
    }

    @Override
    public boolean hasNext(RowView view) {
        return left.hasNext(view) || right.hasNext(view);
    }

    @Override
    public void moveToFirst(RowView view) {
        left.moveToFirst(view);
        right.moveToFirst(view);
    }

    @Override
    public void moveToNone(RowView view) {
        left.moveToNone(view);
        right.moveToNone(view);
    }

    @Override
    public boolean inInNonePos(RowView view) {
        return left.inInNonePos(view) && right.inInNonePos(view);
    }

    @Override
    public boolean moveToNext(RowView view) {
        if (right.moveToNext(view))
            return true;
        boolean canMove = left.moveToNext(view);
        if (canMove)
            right.moveToNone(view);
        return canMove;
    }


    @Override
    public Boolean checkJoinCondition0(RowView view) {
        if (inInNonePos(view))
            return null;

        Boolean leftCondition = left.checkJoinCondition0(view);
        if (leftCondition == null)
            return type.leftNullable;
        Boolean rightCondition = right.checkJoinCondition0(view);
        if (rightCondition == null)
            return type.rightNullable;

        if (!rightCondition || !leftCondition)
            return false;

        var checkResult = condition.call(ExpressionContext.create(tablesView.getDatabase(), view));
        if (checkResult == null)
            return false;
        if (checkResult instanceof Bool bool) {
            return bool.value;
        } else throw new InvalidDBAccessException("Filter should return boolean value");
    }
}
