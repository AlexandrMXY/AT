package ru.mephi.bakinaa.lab3.db.context;

import ru.mephi.bakinaa.lab3.commons.Fun;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.lang.defs.RowDefinition;

import java.util.HashMap;
import java.util.Map;

public class DefaultContext implements Context {
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
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("skip", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("join", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("leftJoin", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("rightJoin", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("fullJoin", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("min", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("max", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("sum", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("groupSize", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("reduce", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("count", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("isEmpty", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("anyMatch", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("allMatch", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("contains", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
        functions.put("insert", ((ctx, args) -> {
            ctx.getDatabase().getTable((Id)args[0]).insert((RowDefinition) args[1]);
            return null;
        }));
        functions.put("removeIf", ((ctx, args) -> {
            throw new UnsupportedOperationException("Unimplemented");
        }));
    }
}
