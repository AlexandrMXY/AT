package ru.mephi.bakinaa.lab3.commons;


import lombok.Getter;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

@Getter
public final class FunCall<T extends Obj> implements Expression {
    private final Fun<T> fun;
    private final Expression[] args;

    public FunCall(Fun<T> fun, Expression... args) {
        this.args = args;
        this.fun = fun;
    }

    public T call(ExpressionContext ctx) {
//        Obj[] objArgs = new Obj[args.length];
//        for (int i = 0; i < args.length; i++)
//            if (args[i] instanceof Obj id)
//                objArgs[i] = id;
//            else
//                objArgs[i] = args[i] == null ? null : args[i].call(ctx);
        return fun.call(ctx, args);
    }
}
