package ru.mephi.bakinaa.regex.tree;

import java.util.HashSet;

public class Star extends TreeNode {
    public Star(TreeNode child) {
        setLeft(child);
    }

    @Override
    protected void calculateOwnPos() {
        if (getRight() != null)
            throw new IllegalStateException();
        nullable = true;

        firstpos = new HashSet<>(getLeft().firstpos);
        lastpos = new HashSet<>(getLeft().lastpos);
    }

    public TreeNode getChild() {
        return getLeft();
    }

    @Override
    public void reverse() {}
}
