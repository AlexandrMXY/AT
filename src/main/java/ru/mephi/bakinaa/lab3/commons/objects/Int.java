package ru.mephi.bakinaa.lab3.commons.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode
public class Int extends SimpleObj {
    public final long value;

    public static Int parse(String str) {
        return new Int(Long.parseLong(str));
    }


    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
