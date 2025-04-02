package ru.mephi.bakinaa.lab3.commons;


public final class FunCall<T extends Obj> implements FunArgument {
    private final FunArgument[] args;
    private final Fun<T> fun;

    public FunCall(Fun<T> fun, FunArgument... args) {
        this.args = args;
        this.fun = fun;
    }

    public T call() {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof FunCall<?> call)
                args[i] = call.call();
        }
        return fun.call(args);
    }
}
