package ru.mephi.bakinaa.lab3.commons;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Expressions {
    private final List<Expression> expressions = new ArrayList<>();

    public Expressions(Expression expression) {
        expressions.add(expression);
    }

    public Expressions add(Expression expression) {
        expressions.add(expression);
        return this;
    }

    public Expressions add(Expressions expressions) {
        this.expressions.addAll(expressions.expressions);
        return this;
    }

    public Obj call(ExpressionContext ctx) {
        Obj res = null;
        for (var expr : expressions)
            res = expr.call(ctx);
        return null;
    }
}
