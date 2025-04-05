package ru.mephi.bakinaa.lab3.commons;

public non-sealed interface Obj extends Expression {
    default Obj call(ExpressionContext ctx) {
        return this;
    }
}
