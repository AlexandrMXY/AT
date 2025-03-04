package ru.mephi.bakinaa.regex.tree;

import java.util.ArrayList;
import java.util.HashSet;

public class Or extends TreeNode {
    public Or(TreeNode left, TreeNode right) {
        addChild(left);
        addChild(right);
        calculatePos();
    }

    @Override
    protected void calculatePos() {
        if (children.size() != 2)
            throw new IllegalStateException();

        firstpos = new HashSet<>(children.getFirst().firstpos);
        firstpos.addAll(children.getLast().firstpos);

        lastpos = new HashSet<>(children.getLast().lastpos);
        lastpos.addAll(children.getFirst().lastpos);

        nullable = children.getFirst().nullable || children.getLast().nullable;
    }
}
