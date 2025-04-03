package ru.mephi.bakinaa.lab3.lang.enums;

import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.commons.objects.Bool;
import ru.mephi.bakinaa.lab3.commons.objects.Int;
import ru.mephi.bakinaa.lab3.commons.objects.Real;
import ru.mephi.bakinaa.lab3.commons.objects.Str;
import ru.mephi.bakinaa.lab3.exceptions.LangException;

import java.util.Objects;
import java.util.function.Predicate;

@RequiredArgsConstructor
public enum Type {
    STRING(o -> o == null ||  o instanceof Str),
    REAL(o -> o == null || o instanceof Real),
    INT(o -> o == null || o instanceof Int),
    BOOL(o -> o == null || o instanceof Bool),
    NULL(Objects::isNull);

    private final Predicate<Obj> isInstance;

    public static Type parse(String str) {
        return switch (str) {
            case "String" -> STRING;
            case "Real" -> REAL;
            case "Integer" -> INT;
            case "Boolean" -> BOOL;

            default -> throw new LangException("Unknown type " + str);
        };
    }

    public boolean isInstance(Obj obj) {
        return isInstance.test(obj);
    };
}
