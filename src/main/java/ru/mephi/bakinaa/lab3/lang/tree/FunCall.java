package ru.mephi.bakinaa.lab3.lang.tree;

public class FunCall extends TreeNode {
    private String name;
    private ExprSet args;

    public FunCall(String name, TreeNode args) {
        this.name = name;
        if (args instanceof ExprSet exprSet)
            this.args = exprSet;
        else
            this.args = new ExprSet(args);
    }

}
