package ru.mephi.bakinaa.regex.tree;

import java.util.HashSet;

public class Concat extends TreeNode {
    public Concat(TreeNode left, TreeNode right) {
        addChild(left);
        addChild(right);
    }

    @Override
    protected void calculateOwnPos() {
        if (children.size() != 2)
            throw new IllegalStateException();

        firstpos = new HashSet<>(children.getFirst().firstpos);
        if (children.getFirst().nullable)
            firstpos.addAll(children.getLast().firstpos);

        lastpos = new HashSet<>(children.getLast().lastpos);
        if (children.getLast().nullable)
            lastpos.addAll(children.getFirst().lastpos);

        nullable = children.getFirst().nullable && children.getLast().nullable;
    }

    public TreeNode left() {
        return children.getFirst();
    }

    public TreeNode right() {
        return children.getLast();
    }
}
