package ru.mephi.bakinaa.lab3.lang.tree.defs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.lang.enums.Modifier;
import ru.mephi.bakinaa.lab3.lang.enums.Types;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;
import ru.mephi.bakinaa.lab3.lang.tree.terms.Id;
import ru.mephi.bakinaa.lab3.lang.tree.terms.ScopedId;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class ColDefinition extends TreeNode {
    private List<Modifier> modifiers = new ArrayList<>();
    private Types type;
    private String name;

    public ColDefinition(Types type, Id name) {
        this.type = type;
        if (name instanceof ScopedId)
            throw new LangException("Illegal col name");
        this.name = name.value;
    }

    public ColDefinition(String type, String name) {
        this.type = Types.parse(type);
        this.name = name;
    }

    public void addModifier(Modifier m) {
        modifiers.add(m);
    }
}
