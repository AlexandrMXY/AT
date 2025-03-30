package ru.mephi.bakinaa.lab3.lang.enums;

import ru.mephi.bakinaa.lab3.exceptions.LangException;

public enum Modifier {
    UNIQUE,
    NOT_NULL,
    PRIMARY;

    public static Modifier parse(String str) {
        return switch (str) {
            case "unique" -> UNIQUE;
            case "primary" -> PRIMARY;
            case "notnull" -> NOT_NULL;

            default -> throw new LangException("Unknown modifier " + str);
        };
    }
}
