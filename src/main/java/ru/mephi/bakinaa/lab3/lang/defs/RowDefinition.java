package ru.mephi.bakinaa.lab3.lang.defs;

import lombok.Getter;
import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RowDefinition extends Obj {
    private final Map<Id, Expression> assigns = new HashMap<>();

    public RowDefinition(Definitions definitions) {
        for (Definition expr : definitions.getDefinitions()) {
            if (expr instanceof Assign assign) {
                if (assigns.containsKey(assign.getLeft()))
                    throw new LangException("Illegal row definition: multiple assigment to col " + assign.getLeft().toString());
                assigns.put(assign.getLeft(), assign.getRight());
            } else throw new LangException("Illegal row definition");
        }
    }
}
