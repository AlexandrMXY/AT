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
        Obj left = call(args[0], ctx);
        Obj right = call(args[1], ctx);
        if (left == null || right == null)
            return Bool.of(right == left);
        if (left instanceof Int left0)
            return Bool.of(left0.value >= ((Int)right).value);
        return Bool.of(((Real)left).value >= ((Real)right).value);
    });
    public static final Fun<Bool> LESS_EQ = ((ctx, args) -> {
        Obj left = call(args[0], ctx);
        Obj right = call(args[1], ctx);
        if (left == null || right == null)
            return Bool.of(right == left);
        if (left instanceof Int left0)
            return Bool.of(left0.value <= ((Int)right).value);
        return Bool.of(((Real)left).value <= ((Real)right).value);
    });
    public static final Fun<Bool> GREATER = ((ctx, args) -> {
        Obj left = call(args[0], ctx);
        Obj right = call(args[1], ctx);
        if (left == null || right == null)
            return Bool.of(left != null);
        if (left instanceof Int left0)
            return Bool.of(left0.value > ((Int)right).value);
        return Bool.of(((Real)left).value > ((Real)right).value);
    });
    public static final Fun<Bool> LESS = ((ctx, args) -> {
        Obj left = call(args[0], ctx);
        Obj right = call(args[1], ctx);
        if (left == null || right == null)
            return Bool.of(right != null);
        if (left instanceof Int left0)
            return Bool.of(left0.value < ((Int)right).value);
        return Bool.of(((Real)left).value < ((Real)right).value);
    });

    public static final Fun<SimpleObj> ADD = (ctx, args) -> {
        Obj left = call(args[0], ctx);
        Obj right = call(args[1], ctx);
        if (left == null)
            left = right instanceof Real ? new Real(0) : new Int(0);
        if (left instanceof Real r)
            return new Real(r.value + realVal(right));
        return new Int(((Int)left).value + intVal(right));
    };
    public static final Fun<SimpleObj> SUB = (ctx, args) -> {
        Obj left = call(args[0], ctx);
        Obj right = call(args[1], ctx);
        if (left == null)
            left = right instanceof Real ? new Real(0) : new Int(0);
        if (left instanceof Real r)
            return new Real(r.value + realVal(right));
        return new Int(((Int)left).value + intVal(right));
    };
    public static final Fun<SimpleObj> MUL = (ctx, args) -> {
        Obj left = call(args[0], ctx);
        Obj right = call(args[1], ctx);
        if (left == null)
            left = right instanceof Real ? new Real(0) : new Int(0);
        if (left instanceof Real r)
            return new Real(r.value * realVal(right));
        return new Int(((Int)left).value * intVal(right));
    };
    public static final Fun<SimpleObj> DIV = (ctx, args) -> {
        Obj left = call(args[0], ctx);
        Obj right = call(args[1], ctx);
        if (left == null)
            left = right instanceof Real ? new Real(0) : new Int(0);
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
        if (left instanceof Real l0) {
            if (right instanceof Real r0)
                return l0.value < r0.value ? r0 : l0;
            if (right instanceof Int r0)
                return l0.value < r0.value ? r0 : l0;
        }
        if (left instanceof Int l0) {
            if (right instanceof Real r0)
                return l0.value < r0.value ? r0 : l0;
            if (right instanceof Int r0)
                return l0.value < r0.value ? r0 : l0;
        }
        return null;
    };
    public static final Fun<?> MIN = (ctx, args) -> {
        Obj left = call(args[0], ctx);
        Obj right = call(args[1], ctx);
        if (left == null)
            return right;
        if (right == null)
            return left;
        if (left instanceof Real l0) {
            if (right instanceof Real r0)
                return l0.value > r0.value ? r0 : l0;
            if (right instanceof Int r0)
                return l0.value > r0.value ? r0 : l0;
        }
        if (left instanceof Int l0) {
            if (right instanceof Real r0)
                return l0.value > r0.value ? r0 : l0;
            if (right instanceof Int r0)
                return l0.value > r0.value ? r0 : l0;
        }
        return null;
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

    public static Fun<?> reference(Id id) {
        return new Fun<Obj>() {
            private final Id target = id;

            @Override
            public Obj call(ExpressionContext ctx, Expression... args) {
                return ctx.get(target);
            }
        };
    }
}
