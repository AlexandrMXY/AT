package ru.mephi.bakinaa.lab3.lang;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.mephi.bakinaa.lab3.commons.Expression;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class FunArgs {
    private final List<Expression> args = new ArrayList<>();


    public FunArgs(Expression expr) {
        this.args.add(expr);
    }

    public FunArgs add(Expression expr) {
        args.add(expr);
        return this;
    }

    public FunArgs addAll(FunArgs second) {
        args.addAll(second.args);
        return this;
    }
}
