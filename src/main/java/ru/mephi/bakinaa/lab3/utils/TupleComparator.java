package ru.mephi.bakinaa.lab3.utils;

import java.io.Serializable;
import java.util.Comparator;

public class TupleComparator implements Serializable, Comparator<Tuple> {
    @Override
    public int compare(Tuple o1, Tuple o2) {
        return Tuple.compare(o1, o2);
    }
}
