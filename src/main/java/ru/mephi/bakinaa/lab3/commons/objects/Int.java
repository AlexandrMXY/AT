package ru.mephi.bakinaa.lab3.commons.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Int extends Num {
    public final long value;

    public static Int parse(String str) {
        return new Int(Long.parseLong(str));
    }

    public int asInt32() {
        if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE)
            throw new InvalidDBAccessException("The number is too big/small");
        return (int) value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
