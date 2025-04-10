package ru.mephi.bakinaa.lab3.utils;

import lombok.EqualsAndHashCode;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;

import java.io.Serializable;

@EqualsAndHashCode
public class Tuple implements Serializable {
    private final SimpleObj[] data;
    public Tuple(int size) {
        data = new SimpleObj[size];
    }

    public SimpleObj get(int i) {
        return data[i];
    }
    public void set(int i, SimpleObj val) {
        data[i] = val;
    }

    public static int compare(Tuple left, Tuple right) {
        if (left.data.length != right.data.length)
            return left.data.length - right.data.length;
        for (int i = 0; i < left.data.length; i++) {
            int cmp = SimpleObj.compare(left.data[i], right.data[i]);
            if (cmp != 0)
                return cmp;
        }
        return 0;
    }
}
