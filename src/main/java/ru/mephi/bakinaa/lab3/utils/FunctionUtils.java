package ru.mephi.bakinaa.lab3.utils;

import lombok.experimental.UtilityClass;
import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.commons.objects.Bool;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

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
}
