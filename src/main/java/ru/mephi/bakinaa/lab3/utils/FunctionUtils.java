package ru.mephi.bakinaa.lab3.utils;

import lombok.experimental.UtilityClass;
import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.commons.FunCall;
import ru.mephi.bakinaa.lab3.commons.Functions;
import ru.mephi.bakinaa.lab3.commons.objects.Bool;
import ru.mephi.bakinaa.lab3.db.Database;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.lang.defs.RowDefinition;

@UtilityClass
public class FunctionUtils {
    public static boolean checkPredicate(ExpressionContext ctx, Expression predicate) {
        var result = predicate.call(ctx);
        if (result == null)
            return false;
        if (result instanceof Bool bool)
            return bool.value;
        throw new InvalidDBAccessException("Boolean value expected");
    }

    public static Expression rowDefinitionAsCondition(Database database, RowDefinition definition) {
        var keys = definition.getAssigns().keySet();
        ExpressionContext context = ExpressionContext.create(database);
        Expression expression = null;
        for (var key : keys) {
            Expression cur = new FunCall<>(Functions.EQ, key, definition.getAssigns().get(key).call(context));
            expression = expression == null ? cur : new FunCall<>(Functions.AND, expression, cur);
        }
        return expression;
    }
}
