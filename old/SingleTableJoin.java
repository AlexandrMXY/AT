package ru.mephi.bakinaa.lab3.db.views__;

import lombok.AllArgsConstructor;
import ru.mephi.bakinaa.lab3.db.core.Table;

@AllArgsConstructor
class SingleTableJoin extends Join {
    int tableIndex;
    private Table table;

    @Override
    public void moveToFirst(RowView view) {
        view.setIndex(tableIndex, 0);
    }

    @Override
    public boolean moveToNext(RowView view) {
        if (hasNext(view)) {
            view.incIndex(tableIndex);
            return true;
        }
        return false;
    }

    @Override
    public void moveToNone(RowView view) {
        view.setIndex(tableIndex, -1);
    }

    @Override
    public boolean hasNext(RowView view) {
        int index = view.getIndex(tableIndex);
        return index + 1 < table.getRowsCnt();
    }

    @Override
    public boolean inInNonePos(RowView view) {
        return view.getIndex(tableIndex) < 0;
    }

    @Override
    public Boolean checkJoinCondition0(RowView view) {
        if (inInNonePos(view))
            return null;
        return true;
    }
}
