package ru.mephi.bakinaa.lab3.lang.defs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.lang.enums.IndexType;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class TableDefinition {
    private IndexType indexType;
    private List<ColDefinition> cols = new ArrayList<>();
    private List<ConstraintDefinition> constraints = new ArrayList<>();
    private Id id;

    public TableDefinition(IndexType index, Definitions definitions, Id id) {
        if (id.scope != null)
            throw new LangException("Illegal table id " + id.toString());
        this.indexType = index;
        this.id = id;
        for (Definition node : definitions.getDefinitions()) {
            if (node instanceof ColDefinition row)
                cols.add(row);
            else if (node instanceof ConstraintDefinition constraint)
                constraints.add(constraint);
            else
                throw new LangException("Illegal table definition");
        }
    }

    public TableDefinition(Definitions definitions, Id id) {
        this(IndexType.NONE, definitions, id);
    }

}
