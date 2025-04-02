package ru.mephi.bakinaa.lab3.lang.tree.defs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.lang.enums.IndexType;
import ru.mephi.bakinaa.lab3.lang.tree.Statements;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class TableDefinition implements TreeNode {
    private IndexType indexType;
    private List<ColDefinition> cols = new ArrayList<>();
    private List<ConstraintDefinition> constraints = new ArrayList<>();
    private Id id;

    public TableDefinition(IndexType index, Statements statements, Id id) {
        if (id.scope != null)
            throw new LangException("Illegal table id " + id.toString());
        this.indexType = index;
        this.id = id;
        for (TreeNode node : statements.getStatements()) {
            if (node instanceof ColDefinition row)
                cols.add(row);
            else if (node instanceof ConstraintDefinition constraint)
                constraints.add(constraint);
            else
                throw new LangException("Illegal table definition");
        }
    }

    public TableDefinition(Statements statements, Id id) {
        this(IndexType.NONE, statements, id);
    }

    public List<TreeNode> getAllRowsAndConstraints() {
        List<TreeNode> res = new ArrayList<>();
        res.addAll(cols);
        res.addAll(constraints);
        return res;
    }
}
