package ru.mephi.bakinaa.lab3.commons.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Real extends Num {
    public final double value;

    public static Real parse(String str) {
        return new Real(Double.parseDouble(str));
    }


    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
