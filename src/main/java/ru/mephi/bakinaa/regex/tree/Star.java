package ru.mephi.bakinaa.regex.tree;

import java.util.ArrayList;
import java.util.HashSet;

public class Star extends TreeNode {
    public Star(TreeNode child) {
        addChild(child);
        calculatePos();
    }

    @Override
    protected void calculatePos() {
        nullable = true;
        if (children.size() != 1)
            throw new IllegalStateException();
        firstpos = new HashSet<>(children.getFirst().firstpos);
        lastpos = new HashSet<>(children.getFirst().lastpos);
    }

    public TreeNode getChild() {
        return children.getFirst();
    }
}
