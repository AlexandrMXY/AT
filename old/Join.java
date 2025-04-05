package ru.mephi.bakinaa.lab3.db.views__;

abstract class Join {
    protected boolean nullable = false;

    abstract boolean hasNext(RowView view);

    abstract void moveToFirst(RowView view);

    abstract void moveToNone(RowView view);

    abstract boolean moveToNext(RowView view);

    abstract boolean inInNonePos(RowView view);

    protected abstract Boolean checkJoinCondition0(RowView view);

    boolean checkJoinCondition(RowView view) {
        Boolean check0 = checkJoinCondition0(view);
        return check0 == null ? nullable : check0;
    }
}
