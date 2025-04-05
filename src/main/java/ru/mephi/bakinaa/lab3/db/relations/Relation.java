package ru.mephi.bakinaa.lab3.db.relations;

import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.commons.objects.Bool;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.Int;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.JoinType;
import ru.mephi.bakinaa.lab3.db.core.Database;

import java.util.List;
import java.util.Set;

public interface Relation extends Obj {
    Id REDUCE_ACCUMULATOR_VARIABLE = new Id("__value");

    Database getDatabase();

    Relation limit(int value);
    Relation skip(int value);
    Relation project(List<Id> cols);
    Relation filter(Expression filter);
    Relation join(Relation other, JoinType type, Expression condition);

    /**
     * Групирует отн по значению столбцов
     * @param columns столбци группировки
     * @param aggregator выражение-аггрегатор: возвращаеит новую строку
     * @return сгрупированное отношение
     */
    Relation group(List<Id> columns, Expression aggregator);

    /**
     * Преобразует строки в другие
     * @param mapper функция преобразования: возвращает новую ситроку
     * @return преобразованое отношение
     */
    Relation map(Expression mapper);

    Int count();
    Bool isEmpty();
    Bool anyMatch(Expression predicate);
    Bool allMatch(Expression predicate);

    Obj reduce(Expression initial, Expression reducer);


    int getSize();
    RowView getByIndex(int index);
    RowView first();
    void moveToIndex(RowView view, int index);

    SimpleObj get(int rowId, Id columnId);
    Set<Id> getColumnsSet();
}
