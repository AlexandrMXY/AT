package ru.mephi.bakinaa.lab3.lang.tree.defs;

import lombok.Getter;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.lang.tree.Statements;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;
import ru.mephi.bakinaa.lab3.lang.tree.ops.Assign;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RowDefinition implements TreeNode {
    private Map<Id, TreeNode> assigns = new HashMap<>();

    public RowDefinition(Statements statements) {
        for (TreeNode expr : statements.getStatements()) {
            if (expr instanceof Assign assign) {
                if (assigns.containsKey(assign.getLeft()))
                    throw new LangException("Illegal row definition: multiple assigment to col " + assign.getLeft().toString());
                assigns.put(assign.getLeft(), assign.getRight());
            } else throw new LangException("Illegal row definition");
        }
    }
}
