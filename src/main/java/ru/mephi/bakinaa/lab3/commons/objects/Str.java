package ru.mephi.bakinaa.lab3.commons.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Str extends SimpleObj {
    public final String value;

    public static Str fromQuotedString(String str) {
        return new Str(str.substring(1, str.length() - 1));
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public String toCsvString() {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
