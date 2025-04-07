package ru.mephi.bakinaa.lab3.db.relations.rows;

import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;

public interface RowView {
    SimpleObj get(Id id);

}
