package ru.mephi.bakinaa.lab3.db.relations.rows;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.relations.Relation;

@Getter
@Setter
@AllArgsConstructor
public class SimpleRowView implements RowView {
    private Relation relation;
    private int index = 0;

    @Override
    public SimpleObj get(Id id) {
        return relation.get(index, id);
    }
}
