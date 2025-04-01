package ru.mephi.bakinaa.lab3.db;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JoinType {
    LEFT(false, true),
    RIGHT(true, false),
    INNER(false, false),
    FULL(true, true);

    public final boolean leftNullable;
    public final boolean rightNullable;
}
