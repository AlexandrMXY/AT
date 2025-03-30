package ru.mephi.bakinaa.lab3.lang.tree.terms;


import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ScopedId extends Id {
    private String scope;

    public ScopedId(String scope, String value) {
        super(value);
        this.scope = scope;
    }

    @Override
    public String toString() {
        return scope + "::" + value;
    }
}
