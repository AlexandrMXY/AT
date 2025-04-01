package ru.mephi.bakinaa.lab3.lang.tree.defs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.lang.enums.Modifier;
import ru.mephi.bakinaa.lab3.lang.enums.Types;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;
import ru.mephi.bakinaa.lab3.commons.Id;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class ColDefinition implements TreeNode {
    private List<Modifier> modifiers = new ArrayList<>();
    private Types type;
    private String name;

    public ColDefinition(Types type, Id name) {
        this.type = type;
        if (name.scope != null)
            throw new LangException("Illegal col name");
        this.name = name.value;
    }

    public ColDefinition(String type, Id name) {
        this.type = Types.parse(type);
        if (name.scope != null)
            throw new LangException("Illegal col name");
        this.name = name.value;
    }

    public ColDefinition(String type, String name) {
        this.type = Types.parse(type);
        this.name = name;
    }

    public void addModifier(Modifier m) {
        if (modifiers.contains(m))
            throw new LangException("Multiple " + m + " modifiers");
        modifiers.add(m);
    }
}
