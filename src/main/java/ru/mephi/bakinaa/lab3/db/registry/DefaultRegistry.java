package ru.mephi.bakinaa.lab3.db.registry;

import org.springframework.stereotype.Component;
import ru.mephi.bakinaa.lab3.commons.*;
import ru.mephi.bakinaa.lab3.commons.objects.Bool;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.Int;
import ru.mephi.bakinaa.lab3.commons.objects.Str;
import ru.mephi.bakinaa.lab3.db.JoinType;
import ru.mephi.bakinaa.lab3.db.relations.Relation;
import ru.mephi.bakinaa.lab3.db.relations.Table;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.lang.defs.ColDefinition;
import ru.mephi.bakinaa.lab3.lang.defs.ConstraintDefinition;
import ru.mephi.bakinaa.lab3.lang.defs.Definitions;
import ru.mephi.bakinaa.lab3.lang.defs.RowDefinition;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class DefaultRegistry implements Registry {
    public static final Id REDUCE_ACCUMULATOR_VARIABLE = Relation.REDUCE_ACCUMULATOR_VARIABLE;
    private final Map<String, Fun<?>> functions = new HashMap<>();

    @Override
    public Fun<?> getFunction(String name) {
        return functions.get(name);
    }

    {
        functions.put("get", ((ctx, args) -> {
            checkArgs(args, 1);
            Relation relation = (Relation) args[0].call(ctx);
            return relation.get();
        }));
        functions.put("match", (((ctx, args) -> {
            checkArgs(args, 2);
            Pattern pattern = Pattern.compile(((Str) args[1].call(ctx)).value);
            return Bool.of(pattern.matcher(((Str) args[0].call(ctx)).value).matches());
        })));
        functions.put("createDatabase", ((ctx, args) -> {
            checkArgs(args, 1);
            Id id = (Id)args[0];
            if (id.scope != null)
                throw new InvalidDBAccessException("Illegal database name");
            ctx.getDatabaseService().createDatabase(id.value);
            return null;
        }));
        functions.put("deleteDatabase", ((ctx, args) -> {
            checkArgs(args, 1);
            Id id = (Id)args[0];
            if (id.scope != null)
                throw new InvalidDBAccessException("Illegal database name");
            ctx.getDatabaseService().removeDatabase(id.value);
            return null;
        }));
        functions.put("addConstraint", ((ctx, args) -> {
            checkArgs(args, 2);
            Table table = ctx.getDatabase().getTable((Id)args[0]);
            Definitions definitions = (Definitions) args[1];
            if (definitions.getDefinitions().size() != 1)
                throw new InvalidDBAccessException("Illegal function call");
            ConstraintDefinition constraintDefinition = (ConstraintDefinition) definitions.getDefinitions().getFirst();
            table.addConstraint(constraintDefinition);
            return null;
        }));
        functions.put("addForeignKey", ((ctx, args) -> {
            throw new UnsupportedOperationException("Use addConstraint instead");
        }));
        functions.put("addColumns", ((ctx, args) -> {
            checkArgs(args, 2);
            List<ColDefinition> colDefinitions = new ArrayList<>();
            Definitions definitions = (Definitions) args[1];
            for (var def : definitions.getDefinitions())
                colDefinitions.add((ColDefinition) def);
            ctx.getDatabase().getTable((Id)args[0]).addColumns(colDefinitions);
            return null;
        }));
        functions.put("editColumn", ((ctx, args) -> {
            Table table = ctx.getDatabase().getTable((Id)args[0]);
            Id colName = (Id) args[1];
            if (colName.scope != null)
                throw new InvalidDBAccessException("Illegal col id");
            Definitions definitions = (Definitions) args[2];
            if (definitions.getDefinitions().size() != 1)
                throw new InvalidDBAccessException("Illegal function call");
            ColDefinition colDefinition = (ColDefinition) definitions.getDefinitions().getFirst();
            table.editColumn(colName.value, colDefinition);
            return null;
        }));
        functions.put("removeColumn", ((ctx, args) -> {
            checkArgs(args, 2);
            ctx.getDatabase().getTable((Id)args[0]).removeColumns(Set.of((Id) args[1]));
            return null;
        }));
        functions.put("removeColumns", ((ctx, args) -> {
            if (args.length < 2)
                throw new InvalidDBAccessException("Illegal function call");
            Set<Id> cols = new HashSet<>();
            for (int i = 1; i < args.length; i++)
                cols.add((Id) args[i]);
            ctx.getDatabase().getTable((Id)args[0]).removeColumns(cols);
            return null;
        }));
        functions.put("deleteConstraint", ((ctx, args) -> {
            checkArgs(args, 2);
            Table table = ctx.getDatabase().getTable((Id)args[0]);
            Id constaintId = (Id) args[1];
            if (constaintId.scope != null)
                throw new InvalidDBAccessException("Illegal constraint name");
            table.removeConstraint(constaintId.value);
            return null;
        }));
        functions.put("deletePrimary", ((ctx, args) -> {
            checkArgs(args, 1);
            Table table = ctx.getDatabase().getTable((Id)args[0]);
            table.removePKey();
            return null;
        }));
        functions.put("deleteRelationship", ((ctx, args) -> {
            checkArgs(args, 1);
            ctx.getDatabase().deleteTable((Id)args[0]);
            return null;
        }));
        functions.put("project", ((ctx, args) -> {
            if (args.length < 2)
                throw new InvalidDBAccessException("Illegal project function usage");
            Set<Id> cols = new HashSet<>();
            for (int i = 1; i < args.length; i++)
                cols.add((Id) args[i]);

            return relation(ctx, args[0]).project(cols);
        }));
        functions.put("map", ((ctx, args) -> {
            checkArgs(args, 2);
            return relation(ctx, args[0]).map((RowDefinition) args[1]);
        }));
        functions.put("sort", ((ctx, args) -> {
            checkArgs(args, 2);
            return relation(ctx, args[0]).sort((Sort) args[1]);
        }));
        functions.put("limit", ((ctx, args) -> {
            checkArgs(args, 2);
            return relation(ctx, args[0]).limit(((Int)args[1].call(ctx)).asInt32());
        }));
        functions.put("skip", ((ctx, args) -> {
            checkArgs(args, 2);
            return relation(ctx, args[0]).skip(((Int)args[1].call(ctx)).asInt32());
        }));
        functions.put("join", ((ctx, args) -> {
            checkArgs(args, 3);
            return relation(ctx, args[0]).join(relation(ctx, args[1]), JoinType.INNER, args[2]);
        }));
        functions.put("leftJoin", ((ctx, args) -> {
            checkArgs(args, 3);
            return relation(ctx, args[0]).join(relation(ctx, args[1]), JoinType.LEFT, args[2]);
        }));
        functions.put("rightJoin", ((ctx, args) -> {
            checkArgs(args, 3);
            return relation(ctx, args[0]).join(relation(ctx, args[1]), JoinType.RIGHT, args[2]);
        }));
        functions.put("fullJoin", ((ctx, args) -> {
            checkArgs(args, 3);
            return relation(ctx, args[0]).join(relation(ctx, args[1]), JoinType.FULL, args[2]);
        }));
        functions.put("group", ((ctx, args) -> {
            if (args.length < 2)
                throw new InvalidDBAccessException("Invalid group function usage.");
            Relation rel = relation(ctx, args[0]);
            Set<Id> cols = new HashSet<>();
            for (int i = 1; i < args.length - 1; i++)
                cols.add((Id) args[i]);
            Expression last = args[args.length - 1];
            RowDefinition aggregator = last instanceof RowDefinition ? (RowDefinition) last : new RowDefinition(new Definitions());
            if (!(last instanceof RowDefinition))
                cols.add((Id) last);
            return rel.group(cols, aggregator);
        }));
        functions.put("min", ((ctx, args) -> {
            checkArgs(args, 1);
            return ctx.getRelation().reduce(null, new FunCall<>(Functions.MIN, REDUCE_ACCUMULATOR_VARIABLE, (Id)args[0]));
        }));
        functions.put("max", ((ctx, args) -> {
            checkArgs(args, 1);
            return ctx.getRelation().reduce(null, new FunCall<>(Functions.MAX, REDUCE_ACCUMULATOR_VARIABLE, (Id)args[0]));
        }));
        functions.put("sum", ((ctx, args) -> {
            checkArgs(args, 1);
            return ctx.getRelation().reduce(null, new FunCall<>(Functions.ADD, REDUCE_ACCUMULATOR_VARIABLE, (Id)args[0]));
        }));
        functions.put("groupSize", ((ctx, args) -> {
            checkArgs(args, 0);
            return ctx.getRelation().count();
        }));
        functions.put("reduce", ((ctx, args) -> {
            checkArgs(args, 2);
            return ctx.getRelation().reduce(args[0], args[1]);
        }));
        functions.put("count", ((ctx, args) -> {
            checkArgs(args, 1);
            return relation(ctx, args[0]).count();
        }));
        functions.put("isEmpty", ((ctx, args) -> {
            checkArgs(args, 1);
            return relation(ctx, args[0]).isEmpty();
        }));
        functions.put("anyMatch", ((ctx, args) -> {
            checkArgs(args, 2);
            return relation(ctx, args[0]).anyMatch(args[1]);
        }));
        functions.put("allMatch", ((ctx, args) -> {
            checkArgs(args, 2);
            return relation(ctx, args[0]).allMatch(args[1]);
        }));
        functions.put("contains", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("findIf", ((ctx, args) -> {
            checkArgs(args, 2);
            return relation(ctx, args[0]).filter(args[1]);
        }));
        functions.put("insert", ((ctx, args) -> {
            checkArgs(args, 2);
            ctx.getDatabase().getTable((Id)args[0]).insert((RowDefinition) args[1].call(ctx));
            return null;
        }));
        functions.put("removeIf", ((ctx, args) -> {
            checkArgs(args, 2);
            ctx.getDatabase().getTable((Id)args[0]).removeIf(args[1]);
            return null;
        }));
        functions.put("removeBy", ((ctx, args) -> {
            checkArgs(args, 2);
            ctx.getDatabase().getTable((Id)args[0]).removeBy((RowDefinition) args[1]);
            return null;
        }));
        functions.put("findBy", ((ctx, args) -> {
            checkArgs(args, 2);
            return relation(ctx, args[0]).findBy((RowDefinition) args[1]);
        }));
        functions.put("findAll", ((ctx, args) -> {
            checkArgs(args, 1);
            return relation(ctx, args[0]);
        }));
    }

    private static Relation relation(ExpressionContext ctx, Expression expression) {
        if (expression instanceof Id id)
            return ctx.getDatabase().getTable(id);
        return (Relation) expression.call(ctx);
    }

    private static void checkArgs(Expression[] args, int expected) {
        if (args.length != expected)
            throw new InvalidDBAccessException("Illegal function call");
    }
}
