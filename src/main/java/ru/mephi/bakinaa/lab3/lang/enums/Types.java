package ru.mephi.bakinaa.lab3.lang.enums;

import ru.mephi.bakinaa.lab3.exceptions.LangException;

public enum Types {
    STRING,
    REAL,
    INT,
    BOOL,
    NULL;

    public static Types parse(String str) {
        return switch (str) {
            case "String" -> STRING;
            case "Real" -> REAL;
            case "Integer" -> INT;
            case "Boolean" -> BOOL;

            default -> throw new LangException("Unknown type " + str);
        };
    }
}
