package ru.mephi.bakinaa.lab3.commons.objects;

import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

import java.io.Serializable;

public abstract class SimpleObj implements Obj, Serializable {
    public String toCsvString() {
        return toString();
    }

    public static int compare(SimpleObj left, SimpleObj right) {
        if (left == null)
            return right == null ? 0 : -1;
        if (right == null)
            return 1;

        return switch (left) {
            case Int o -> Long.compare(o.value, ((Int)right).value);
            case Real o -> Double.compare(o.value, ((Real)right).value);
            case Str o -> o.value.compareTo(((Str)right).value);
            case Bool o -> Boolean.compare(o.value, ((Bool)right).value);
            default -> throw new InvalidDBAccessException("Comparison error");
        };
    }
}
