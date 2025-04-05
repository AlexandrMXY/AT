package ru.mephi.bakinaa.lab3.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.db.core.Database;
import ru.mephi.bakinaa.lab3.db.relations.RowView;
import ru.mephi.bakinaa.lab3.db.relations.SimpleRowView;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ExpressionContext {
    private Database database;
    @Setter
    private RowView row;
    private Map<Id, Obj> params;

    public Obj get(Id id) {
        return params.getOrDefault(id, row.get(id));
    }

    public static ExpressionContext create(Database db) {
        return create(db, null);
    }
    public static ExpressionContext create(Database db, RowView row) {
        return new ExpressionContext(db, row, new HashMap<>());
    }
//    public static ExpressionContext create(RowView row) {
//        return create(null, row);
//    }
    public static ExpressionContext create() {
        return create(null, null);
    }

    public void setValue(Id id, Obj value) {
        params.put(id, value);
    }
}
