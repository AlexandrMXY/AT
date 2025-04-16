package ru.mephi.bakinaa.lab3.db.relations.index;

import ru.mephi.bakinaa.lab3.utils.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderedIndex implements Index {
    private final List<Tuple> tuples = new ArrayList<>();
    private final List<Integer> indexes = new ArrayList<>();

    @Override
    public int findById(Tuple keyRow) {
        int orderIndex = Collections.binarySearch(tuples, keyRow, Tuple::compare);
        if (orderIndex < 0)
            return -1;
        return indexes.get(orderIndex);
    }

    @Override
    public void save(Tuple key, int index) {
        int orderIndex = Collections.binarySearch(tuples, key, Tuple::compare);
        if (orderIndex < 0) {
            int insertionIndex = -(orderIndex + 1);
            tuples.add(insertionIndex, key);
            indexes.add(insertionIndex, index);
        } else {
            tuples.set(orderIndex, key);
            indexes.set(orderIndex, index);
        }
    }

    @Override
    public void delete(Tuple key) {
        int orderIndex = Collections.binarySearch(tuples, key, Tuple::compare);
        if (orderIndex < 0)
            return;
        tuples.remove(orderIndex);
        indexes.remove(orderIndex);
    }
}
