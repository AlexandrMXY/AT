package ru.mephi.bakinaa.lab3.db.relations.index;

import ru.mephi.bakinaa.lab3.db.relations.rows.Row;
import ru.mephi.bakinaa.lab3.utils.Tuple;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapIndex implements Index {
    private final Map<Tuple, Integer> index;

    private MapIndex(Map<Tuple, Integer> map) {
        this.index = map;
    }

    public static MapIndex createHash() {
        return new MapIndex(new HashMap<>());
    }

    public static MapIndex createTree() {
        return new MapIndex(new TreeMap<>(Tuple::compare));
    }

    @Override
    public int findById(Tuple keyRow) {
        return index.getOrDefault(keyRow, -1);
    }

    @Override
    public void save(Tuple key, int index) {
        this.index.put(key, index);
    }

    @Override
    public void delete(Tuple key) {
        index.remove(key);
    }
}
