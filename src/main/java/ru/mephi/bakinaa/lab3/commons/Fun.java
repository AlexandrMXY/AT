package ru.mephi.bakinaa.lab3.commons;

public interface Fun<T extends Obj> {
    T call(ExpressionContext ctx, Expression... args);
}
