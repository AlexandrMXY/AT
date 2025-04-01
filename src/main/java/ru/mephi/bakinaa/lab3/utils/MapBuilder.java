package ru.mephi.bakinaa.lab3.utils;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {
    private Map<K, V> map;

    public MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    public static <K, V> MapBuilder<K, V> builder() {
        return new MapBuilder<>(new HashMap<>());
    }


    public MapBuilder<K, V> put(K k, V v) {
        map.put(k, v);
        return this;
    }

    public Map<K, V> build() {
        return map;
    }
}
