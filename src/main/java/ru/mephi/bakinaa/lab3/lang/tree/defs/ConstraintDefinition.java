package ru.mephi.bakinaa.lab3.lang.tree.defs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.lang.enums.Constraints;
import ru.mephi.bakinaa.lab3.lang.tree.ExprSet;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;
import ru.mephi.bakinaa.lab3.lang.tree.terms.Id;

@AllArgsConstructor
@Getter
public class ConstraintDefinition extends TreeNode {
    private Constraints constraint;
    private ExprSet args;

    public static ConstraintDefinition parse(String str, ExprSet args) {
        return switch (str) {
            case "Unique" -> new ConstraintDefinition(Constraints.UNIQUE, args);
            case "Primary" -> new ConstraintDefinition(Constraints.PRIMARY_KEY, args);
            case "Predicate" -> new ConstraintDefinition(Constraints.PREDICATE, args);

            default -> throw new LangException("Unknown constraint " + str);
        };
    }

    public static ConstraintDefinition foreignKey(Id from, Id to) {
        return new ConstraintDefinition(Constraints.FOREIGN_KEY, new ExprSet(from).add(to));
    }
}
