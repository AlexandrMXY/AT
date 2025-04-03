package ru.mephi.bakinaa.lab3.commons;

import lombok.experimental.UtilityClass;
import ru.mephi.bakinaa.lab3.commons.objects.Bool;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.Int;
import ru.mephi.bakinaa.lab3.commons.objects.Real;
import ru.mephi.bakinaa.lab3.lang.defs.TableDefinition;

import java.util.Objects;

@UtilityClass
public class Functions {
    public static final Fun<Bool> AND = (ctx, args) -> Bool.of(((Bool)args[0]).value && ((Bool)args[1]).value);
    public static final Fun<Bool> OR  = (ctx, args) -> Bool.of(((Bool)args[0]).value || ((Bool)args[1]).value);
    public static final Fun<Bool> NOT = (ctx, args) -> Bool.of(!((Bool)args[0]).value);

    public static final Fun<Bool> LESS = ((ctx, args) -> Bool.of(
            args[0] instanceof Int ?
                    ((Int)args[0]).value < ((Int)args[1]).value :
                    ((Real)args[0]).value < ((Real)args[1]).value
    ));
    public static final Fun<Bool> GREATER = ((ctx, args) -> Bool.of(
            args[0] instanceof Int ?
                    ((Int)args[0]).value > ((Int)args[1]).value :
                    ((Real)args[0]).value > ((Real)args[1]).value
    ));
    public static final Fun<Bool> LESS_EQ = ((ctx, args) -> Bool.of(
            args[0] instanceof Int ?
                    ((Int)args[0]).value <= ((Int)args[1]).value :
                    ((Real)args[0]).value <= ((Real)args[1]).value
    ));
    public static final Fun<Bool> GREATER_EQ = ((ctx, args) -> Bool.of(
            args[0] instanceof Int ?
                    ((Int)args[0]).value >= ((Int)args[1]).value :
                    ((Real)args[0]).value >= ((Real)args[1]).value
    ));
    public static final Fun<Bool> EQ = ((ctx, args) -> Bool.of(
            Objects.equals(args[0], args[1])
    ));
    public static final Fun<Bool> NOT_EQ = ((ctx, args) -> Bool.of(
            !Objects.equals(args[0], args[1])
    ));

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
