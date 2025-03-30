package ru.mephi.bakinaa.lab3.lang.enums;

import ru.mephi.bakinaa.lab3.exceptions.LangException;

public enum IndexType {
    ORDERED,
    HASHTABLE,
    TREE,
    NONE;

    public static IndexType parse(String str) {
        return switch (str) {
            case "ordered" -> IndexType.ORDERED;
            case "hashtable" ->  IndexType.HASHTABLE;
            case "tree" ->  IndexType.TREE;

            default -> throw new LangException("Unknown index type " + str);
        };
    }
}
