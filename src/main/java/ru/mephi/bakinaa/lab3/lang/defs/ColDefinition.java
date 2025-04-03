package ru.mephi.bakinaa.lab3.lang.defs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.lang.enums.Modifier;
import ru.mephi.bakinaa.lab3.lang.enums.Type;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class ColDefinition implements Definition {
    private List<Modifier> modifiers = new ArrayList<>();
    private Type type;
    private String name;

    public ColDefinition(Type type, Id name) {
        this.type = type;
        if (name.scope != null)
            throw new LangException("Illegal col name");
        this.name = name.value;
    }

    public ColDefinition(String type, Id name) {
        this.type = Type.parse(type);
        if (name.scope != null)
            throw new LangException("Illegal col name");
        this.name = name.value;
    }

    public ColDefinition(String type, String name) {
        this.type = Type.parse(type);
        this.name = name;
    }

    public void addModifier(Modifier m) {
        if (modifiers.contains(m))
            throw new LangException("Multiple " + m + " modifiers");
        modifiers.add(m);
    }
}
