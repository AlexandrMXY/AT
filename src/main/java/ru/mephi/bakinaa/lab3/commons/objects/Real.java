package ru.mephi.bakinaa.lab3.commons.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode
public class Real extends SimpleObj {
    public final double value;

    public static Real parse(String str) {
        return new Real(Double.parseDouble(str));
    }


    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
