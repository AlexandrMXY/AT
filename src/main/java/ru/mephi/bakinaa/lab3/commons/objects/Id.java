package ru.mephi.bakinaa.lab3.commons.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

import java.io.Serializable;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Id implements Obj, Serializable {
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


    public String getNonScoped() {
        if (scope != null)
            throw new InvalidDBAccessException("Non scoped id expected");
        return value;
    }
}
