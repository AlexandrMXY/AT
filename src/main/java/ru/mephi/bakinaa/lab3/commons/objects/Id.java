package ru.mephi.bakinaa.lab3.commons.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.commons.Obj;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Id extends Obj {
    public String scope = null;
    public String value;

    public Id(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return scope == null ? value : scope + "::" + value;
    }

    @Override
    public Obj call(ExpressionContext ctx) {
        return ctx.get(this);
    }
}
