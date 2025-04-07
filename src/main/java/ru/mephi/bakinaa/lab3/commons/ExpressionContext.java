package ru.mephi.bakinaa.lab3.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.db.Database;
import ru.mephi.bakinaa.lab3.db.relations.Relation;
import ru.mephi.bakinaa.lab3.db.relations.rows.RowView;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ExpressionContext {
    private Database database;
    private Relation relation;
    @Setter
    private RowView row;
    private Map<Id, Obj> params;

    public Obj get(Id id) {
        return params.containsKey(id) ? params.get(id) : row.get(id);
    }

    public static ExpressionContext create(Relation relation) {
        return new ExpressionContext(relation.getDatabase(), relation, null, null);
    }
    public static ExpressionContext create(Database db) {
        return create(db, null);
    }
    public static ExpressionContext create(Database db, RowView row) {
        return new ExpressionContext(db, null, row, new HashMap<>());
    }
    public static ExpressionContext create() {
        return create(null, null);
    }

    public void setValue(Id id, Obj value) {
        params.put(id, value);
    }
}
