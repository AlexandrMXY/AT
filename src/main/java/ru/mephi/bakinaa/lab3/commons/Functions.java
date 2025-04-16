package ru.mephi.bakinaa.lab3.commons;

import lombok.experimental.UtilityClass;
import ru.mephi.bakinaa.lab3.commons.objects.*;
import ru.mephi.bakinaa.lab3.lang.defs.TableDefinition;

import java.util.Objects;

@UtilityClass
public class Functions {
    public static final Fun<Bool> AND = (ctx, args) -> Bool.of(((Bool)call(args[0], ctx)).value && ((Bool)call(args[1], ctx)).value);
    public static final Fun<Bool> OR  = (ctx, args) -> Bool.of(((Bool)call(args[0], ctx)).value || ((Bool)call(args[1], ctx)).value);
    public static final Fun<Bool> NOT = (ctx, args) -> Bool.of(!((Bool)call(args[0], ctx)).value);

    public static final Fun<Bool> EQ = ((ctx, args) -> Bool.of(
            Objects.equals(call(args[0], ctx), call(args[1], ctx))
    ));
    public static final Fun<Bool> NOT_EQ = ((ctx, args) -> Bool.of(
            !Objects.equals(call(args[0], ctx), call(args[1], ctx))
    ));
    public static final Fun<Bool> GREATER_EQ = ((ctx, args) -> {
        SimpleObj left  = (SimpleObj) call(args[0], ctx);
        SimpleObj right = (SimpleObj) call(args[1], ctx);
        return Bool.of(SimpleObj.compare(left, right) >= 0);
    });
    public static final Fun<Bool> LESS_EQ = ((ctx, args) -> {
        SimpleObj left  = (SimpleObj) call(args[0], ctx);
        SimpleObj right = (SimpleObj) call(args[1], ctx);
        return Bool.of(SimpleObj.compare(left, right) <= 0);
    });
    public static final Fun<Bool> GREATER = ((ctx, args) -> {
        SimpleObj left  = (SimpleObj) call(args[0], ctx);
        SimpleObj right = (SimpleObj) call(args[1], ctx);
        return Bool.of(SimpleObj.compare(left, right) > 0);
    });
    public static final Fun<Bool> LESS = ((ctx, args) -> {
        SimpleObj left  = (SimpleObj) call(args[0], ctx);
        SimpleObj right = (SimpleObj) call(args[1], ctx);
        return Bool.of(SimpleObj.compare(left, right) < 0);
    });

    public static final Fun<SimpleObj> ADD = (ctx, args) -> {
        SimpleObj left  = (SimpleObj) call(args[0], ctx);
        SimpleObj right = (SimpleObj) call(args[1], ctx);
        if (left == null)
            return right;
        if (right == null)
            return left;
        if (left instanceof Real r)
            return new Real(r.value + realVal(right));
        return new Int(((Int)left).value + intVal(right));
    };
    public static final Fun<SimpleObj> SUB = (ctx, args) -> {
        SimpleObj left  = (SimpleObj) call(args[0], ctx);
        SimpleObj right = (SimpleObj) call(args[1], ctx);
        if (left == null)
            return right;
        if (right == null)
            return left;
        if (left instanceof Real r)
            return new Real(r.value - realVal(right));
        return new Int(((Int)left).value - intVal(right));
    };
    public static final Fun<SimpleObj> MUL = (ctx, args) -> {
        SimpleObj left  = (SimpleObj) call(args[0], ctx);
        SimpleObj right = (SimpleObj) call(args[1], ctx);
        if (left == null)
            return right;
        if (right == null)
            return left;
        if (left instanceof Real r)
            return new Real(r.value * realVal(right));
        return new Int(((Int)left).value * intVal(right));
    };
    public static final Fun<SimpleObj> DIV = (ctx, args) -> {
        SimpleObj left  = (SimpleObj) call(args[0], ctx);
        SimpleObj right = (SimpleObj) call(args[1], ctx);
        if (left == null)
            return right;
        if (right == null)
            return left;
        if (left instanceof Real r)
            return new Real(r.value / realVal(right));
        return new Int(((Int)left).value / intVal(right));
    };
    public static final Fun<SimpleObj> NEG = (ctx, args) -> {
        Obj obj = call(args[0], ctx);
        if (obj == null)
            return null;
        if (obj instanceof Real r)
            return new Real(-r.value);
        return new Int(-((Int)obj).value);
    };

    public static final Fun<?> MAX = (ctx, args) -> {
        Obj left = call(args[0], ctx);
        Obj right = call(args[1], ctx);
        if (left == null)
            return right;
        if (right == null)
            return left;

        int cmp = SimpleObj.compare((SimpleObj) left, (SimpleObj) right);
        return (Obj) (cmp >= 0 ? left : right);
    };

    public static final Fun<?> MIN = (ctx, args) -> {
        Obj left = call(args[0], ctx);
        Obj right = call(args[1], ctx);
        if (left == null)
            return right;
        if (right == null)
            return left;

        int cmp = SimpleObj.compare((SimpleObj) left, (SimpleObj) right);
        return (Obj) (cmp <= 0 ? left : right);
    };


    private static long intVal(Obj o) {
        if (o == null)
            return 0;
        if (o instanceof Real r)
            return (long) r.value;
        return ((Int)o).value;
    }

    private static double realVal(Obj o) {
        if (o == null)
            return 0.0;
        if (o instanceof Int i)
            return (long) i.value;
        return ((Real)o).value;
    }

    private static Obj call(Expression expression, ExpressionContext ctx) {
        return expression == null ? null : expression.call(ctx);
    }
    

    public static Fun<?> createTable(TableDefinition tableDefinition) {
        return new Fun<Obj>() {
            private final TableDefinition definition = tableDefinition;

            @Override
            public Obj call(ExpressionContext ctx, Expression... args) {
                ctx.getDatabase().createTable(definition);
                return null;
            }
        };
    }
}
