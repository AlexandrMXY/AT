package ru.mephi.bakinaa.lab3.db.relations.index;

import ru.mephi.bakinaa.lab3.db.relations.rows.Row;
import ru.mephi.bakinaa.lab3.utils.Tuple;

import java.util.List;

public interface Index {
    int findById(Tuple keyRow);
    void save(Tuple key, int index);
    void delete(Tuple key);
}
