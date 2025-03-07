package ru.mephi.bakinaa.regex.tree;

import java.util.HashSet;

public class Or extends TreeNode {
    public Or(TreeNode left, TreeNode right) {
        setLeft(left);
        setRight(right);
    }

    @Override
    protected void calculateOwnPos() {
        if (left == null || right == null)
            throw new IllegalStateException();

        firstpos = new HashSet<>(left.firstpos);
        firstpos.addAll(right.firstpos);

        lastpos = new HashSet<>(right.lastpos);
        lastpos.addAll(left.lastpos);

        nullable = left.nullable || right.nullable;
    }
}
