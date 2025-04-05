package ru.mephi.bakinaa.lab3.db.context;

import ru.mephi.bakinaa.lab3.commons.*;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.Int;
import ru.mephi.bakinaa.lab3.commons.objects.Real;
import ru.mephi.bakinaa.lab3.db.JoinType;
import ru.mephi.bakinaa.lab3.db.relations.Relation;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.lang.defs.RowDefinition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefaultRegistry implements Registry {
    public static final Id REDUCE_ACCUMULATOR_VARIABLE = Relation.REDUCE_ACCUMULATOR_VARIABLE;
    private final Map<String, Fun<?>> functions = new HashMap<>();

    @Override
    public Fun<?> getFunction(String name) {
        return functions.get(name);
    }

    {
        functions.put("createDatabase", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("deleteDatabase", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("addConstraint", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("addForeignKey", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("addColumns", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("editColumn", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("removeColumn", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("removeColumns", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("deleteConstraint", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("deleteRelationship", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("project", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("map", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("sort", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("limit", ((ctx, args) -> {
            return relation(ctx, args[0]).limit(((Int)args[1].call(ctx)).asInt32());
        }));
        functions.put("skip", ((ctx, args) -> {
            return relation(ctx, args[0]).skip(((Int)args[1].call(ctx)).asInt32());
        }));
        functions.put("join", ((ctx, args) -> {
            return relation(ctx, args[0]).join(relation(ctx, args[1]), JoinType.INNER, args[2]);
        }));
        functions.put("leftJoin", ((ctx, args) -> {
            return relation(ctx, args[0]).join(relation(ctx, args[1]), JoinType.LEFT, args[2]);
        }));
        functions.put("rightJoin", ((ctx, args) -> {
            return relation(ctx, args[0]).join(relation(ctx, args[1]), JoinType.RIGHT, args[2]);
        }));
        functions.put("fullJoin", ((ctx, args) -> {
            return relation(ctx, args[0]).join(relation(ctx, args[1]), JoinType.FULL, args[2]);
        }));
        functions.put("group", ((ctx, args) -> {
            if (args.length < 3)
                throw new InvalidDBAccessException("Invalid group function usage.");
            Relation rel = relation(ctx, args[0]);
            Set<Id> cols = new HashSet<>();
            for (int i = 1; i < args.length - 1; i++)
                cols.add((Id) args[i]);
            RowDefinition aggregator = (RowDefinition) args[args.length - 1];
            return rel.group(cols, aggregator);
        }));
        functions.put("min", ((ctx, args) -> {
            return ctx.getRelation().reduce(null, new FunCall<>(Functions.MIN, REDUCE_ACCUMULATOR_VARIABLE, (Id)args[0]));
        }));
        functions.put("max", ((ctx, args) -> {
            return ctx.getRelation().reduce(null, new FunCall<>(Functions.MAX, REDUCE_ACCUMULATOR_VARIABLE, (Id)args[0]));
        }));
        functions.put("sum", ((ctx, args) -> {
            return ctx.getRelation().reduce(null, new FunCall<>(Functions.ADD, REDUCE_ACCUMULATOR_VARIABLE, (Id)args[0]));
        }));
        functions.put("groupSize", ((ctx, args) -> {
            return ctx.getRelation().count();
        }));
        functions.put("reduce", ((ctx, args) -> {
            return ctx.getRelation().reduce(args[0], args[1]);
        }));
        functions.put("count", ((ctx, args) -> {
            return relation(ctx, args[0]).count();
        }));
        functions.put("isEmpty", ((ctx, args) -> {
            return relation(ctx, args[0]).isEmpty();
        }));
        functions.put("anyMatch", ((ctx, args) -> {
            return relation(ctx, args[0]).anyMatch(args[1]);
        }));
        functions.put("allMatch", ((ctx, args) -> {
            return relation(ctx, args[0]).allMatch(args[1]);
        }));
        functions.put("contains", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("insert", ((ctx, args) -> {
            ctx.getDatabase().getTable((Id)args[0]).insert((RowDefinition) args[1].call(ctx));
            return null;
        }));
        functions.put("removeIf", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
    }

    private static Relation relation(ExpressionContext ctx, Expression expression) {
        if (expression instanceof Id id)
            return ctx.getDatabase().getTable(id);
        return (Relation) expression;
    }
}
