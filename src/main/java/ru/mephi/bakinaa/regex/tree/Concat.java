package ru.mephi.bakinaa.regex.tree;

import java.util.HashSet;

public class Concat extends TreeNode {
    public Concat(TreeNode left, TreeNode right) {
        setLeft(left);
        setRight(right);
    }

    @Override
    protected void calculateOwnPos() {
        if (left == null || right == null)
            throw new IllegalStateException();

        firstpos = new HashSet<>(left.firstpos);
        if (left.nullable)
            firstpos.addAll(right.firstpos);

        lastpos = new HashSet<>(right.lastpos);
        if (right.nullable)
            lastpos.addAll(left.lastpos);

        nullable = right.nullable && left.nullable;
    }
}
