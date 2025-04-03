package ru.mephi.bakinaa.lab3.commons;

public sealed interface Expression permits FunCall, Obj {
    Obj call(ExpressionContext ctx);
}
