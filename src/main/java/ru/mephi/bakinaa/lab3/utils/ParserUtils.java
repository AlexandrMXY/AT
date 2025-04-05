package ru.mephi.bakinaa.lab3.utils;

import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.lab3.commons.*;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.db.context.Registry;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.lang.FunArgs;
import ru.mephi.bakinaa.lab3.lang.YYParserVal;
import ru.mephi.bakinaa.lab3.lang.defs.TableDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class ParserUtils {
    private final Registry ctx;


    public YYParserVal statement(YYParserVal left, YYParserVal right) {
        ((Expressions)left.obj).add((Expressions) right.obj);
        return left;
    }
    public YYParserVal statement(YYParserVal arg) {
        Object obj = arg.obj;
        if (obj instanceof Expression expr)
            return new YYParserVal(new Expressions(expr));
        if (obj instanceof TableDefinition definition)
            return new YYParserVal(new Expressions(new FunCall<>(Functions.createTable(definition))));

        throw new LangException();
    }

    public YYParserVal fun(YYParserVal nameVal, FunArgs args) {
        Fun<?> fun = getFun(nameVal);
        return new YYParserVal(new FunCall<>(fun, args.getArgs().toArray(new Expression[0])));
    }
    public YYParserVal fun(YYParserVal nameVal, YYParserVal caller, FunArgs args) {
        Fun<?> fun = getFun(nameVal);
        List<Expression> argList = new ArrayList<>();
        argList.add((Expression) caller.obj);
        argList.addAll(args.getArgs());
        return new YYParserVal(new FunCall<>(fun, argList.toArray(Expression[]::new)));
    }

    public YYParserVal fun(String name, YYParserVal... args) {
        Fun<?> fun = ctx.getFunction(name);
        if (fun == null)
            throw new LangException("Unknown function " + name);
        return fun(fun, args);
    }

    public YYParserVal fun(Fun<? extends Obj> fun, YYParserVal... args) {
        return new YYParserVal(new FunCall<>(fun,
                Arrays.stream(args)
                        .map(arg -> (Expression)arg.obj)
                        .toArray(Expression[]::new)));
    }

    public Fun<?> getFun(YYParserVal idVal) {
        Id name = (Id)idVal.obj;
        if (name.scope != null)
            throw new LangException("Illegal function name " + name);
        Fun<?> fun = ctx.getFunction(name.value);
        if (fun == null)
            throw new LangException("Unknown function " + name);
        return fun;
    }
}
