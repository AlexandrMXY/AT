package ru.mephi.bakinaa.lab3.lang.tree;

import lombok.Getter;
import ru.mephi.bakinaa.lab3.lang.tree.terms.Id;

@Getter
public class FunCall extends TreeNode {
    private TreeNode caller;
    private Id id;
    private ExprSet args;

    public FunCall(Id name, TreeNode args) {
        this(null, name, args);
    }

    public FunCall(TreeNode caller, Id id, TreeNode args) {
        this.caller = caller;
        this.id = id;
        if (args instanceof ExprSet exprSet)
            this.args = exprSet;
        else
            this.args = new ExprSet(args);
    }
}
