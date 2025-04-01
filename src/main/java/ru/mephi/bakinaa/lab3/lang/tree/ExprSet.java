package ru.mephi.bakinaa.lab3.lang.tree;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ExprSet implements TreeNode {
    private List<TreeNode> exprs = new ArrayList<>();

    public ExprSet(TreeNode expr) {
        this.exprs.add(expr);
    }

    public ExprSet add(TreeNode expr) {
        if (expr instanceof ExprSet e)
            exprs.addAll(e.exprs);
        else
            exprs.add(expr);
        return this;
    }

    public static ExprSet combine(TreeNode left, TreeNode right) {
        if (left instanceof ExprSet exprSet)
            return exprSet.add(right);
        return new ExprSet(left).add(right);

    }
}
