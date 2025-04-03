package ru.mephi.bakinaa.lab3.commons;

public non-sealed abstract class Obj implements Expression {
    @Override
    public Obj call(ExpressionContext ctx) {
        return this;
    }
}
