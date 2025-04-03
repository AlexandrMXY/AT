package ru.mephi.bakinaa.lab3.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.db.core.Database;
import ru.mephi.bakinaa.lab3.db.views.RowView;

@AllArgsConstructor
public class ExpressionContext {
    @Getter
    private Database database;
    private RowView row;

    public Obj get(Id id) {
        return row.get(id);
    }

    public static ExpressionContext forDatabase(Database db) {
        return new ExpressionContext(db, null);
    }
}
