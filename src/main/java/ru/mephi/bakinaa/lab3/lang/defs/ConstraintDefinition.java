package ru.mephi.bakinaa.lab3.lang.defs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.lang.FunArgs;
import ru.mephi.bakinaa.lab3.lang.enums.Constraints;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

@Getter
public class ConstraintDefinition implements Definition {
    private final Constraints constraint;
    private final Id id;
    private final FunArgs args;

    public ConstraintDefinition(Constraints constraint, Id id, FunArgs args) {
        if (id.scope != null)
            throw new LangException("Illegal constraint name");
        this.constraint = constraint;
        this.id = id;
        this.args = args;
    }

    public static ConstraintDefinition parse(String str, FunArgs args, Id id) {
        return switch (str) {
            case "Unique" -> new ConstraintDefinition(Constraints.UNIQUE, id, args);
            case "Primary" -> new ConstraintDefinition(Constraints.PRIMARY_KEY, id, args);
            case "Predicate" -> new ConstraintDefinition(Constraints.PREDICATE, id, args);
            case "Foreign" -> new ConstraintDefinition(Constraints.FOREIGN_KEY, id, args);

            default -> throw new LangException("Unknown constraint " + str);
        };
    }

    public static ConstraintDefinition foreignKey(Id from, Id to, Id constraintId) {
        return new ConstraintDefinition(Constraints.FOREIGN_KEY, constraintId, new FunArgs(from).add(to));
    }

}
