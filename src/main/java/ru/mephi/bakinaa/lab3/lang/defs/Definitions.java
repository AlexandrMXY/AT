package ru.mephi.bakinaa.lab3.lang.defs;

import lombok.Getter;
import ru.mephi.bakinaa.lab3.commons.Obj;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Definitions implements Obj {
    private final List<Definition> definitions = new ArrayList<>();

    public Definitions(Definition definition) {
        definitions.add(definition);
    }

    public Definitions add(Definition definition) {
        definitions.add(definition);
        return this;
    }

    public Definitions add(Definitions definitions) {
        this.definitions.addAll(definitions.definitions);
        return this;
    }
}
