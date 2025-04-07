package ru.mephi.bakinaa.lab3.db.relations;

import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.commons.Sort;
import ru.mephi.bakinaa.lab3.commons.objects.Bool;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.Int;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.JoinType;
import ru.mephi.bakinaa.lab3.db.Database;
import ru.mephi.bakinaa.lab3.db.relations.rows.RowView;
import ru.mephi.bakinaa.lab3.lang.defs.RowDefinition;

import java.util.Set;

public interface Relation extends Obj {
    Id REDUCE_ACCUMULATOR_VARIABLE = new Id("__value");

    Database getDatabase();

    Relation limit(int value);
    Relation skip(int value);
    Relation sort(Sort sort);
    Relation project(Set<Id> cols);
    Relation filter(Expression filter);
    Relation join(Relation other, JoinType type, Expression condition);
    Relation findBy(RowDefinition definition);

    /**
     * Групирует отн по значению столбцов
     * @param columns столбци группировки
     * @param aggregator выражение-аггрегатор: возвращаеит новую строку
     * @return сгрупированное отношение
     */
    Relation group(Set<Id> columns, RowDefinition aggregator);

    /**
     * Преобразует строки в другие
     * @param mapper функция преобразования: возвращает новую ситроку
     * @return преобразованое отношение
     */
    Relation map(RowDefinition mapper);

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
    boolean hasColumn(Id col);
}
