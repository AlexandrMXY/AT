package ru.mephi.bakinaa.lab3.lang.defs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.lang.FunArgs;
import ru.mephi.bakinaa.lab3.lang.enums.Constraints;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

@AllArgsConstructor
@Getter
public class ConstraintDefinition implements Definition {
    private Constraints constraint;
    private FunArgs args;

    public static ConstraintDefinition parse(String str, FunArgs args) {
        return switch (str) {
            case "Unique" -> new ConstraintDefinition(Constraints.UNIQUE, args);
            case "Primary" -> new ConstraintDefinition(Constraints.PRIMARY_KEY, args);
            case "Predicate" -> new ConstraintDefinition(Constraints.PREDICATE, args);

            default -> throw new LangException("Unknown constraint " + str);
        };
    }

    public static ConstraintDefinition foreignKey(Id from, Id to) {
        return new ConstraintDefinition(Constraints.FOREIGN_KEY, new FunArgs(from).add(to));
    }
}
