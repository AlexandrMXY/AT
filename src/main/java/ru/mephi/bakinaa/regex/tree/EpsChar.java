package ru.mephi.bakinaa.regex.tree;

import java.util.HashSet;

public class EpsChar extends TreeNode {
    public EpsChar() {
        calculateOwnPos();
    }

    @Override
    protected void calculateOwnPos() {
        nullable = true;
        firstpos = new HashSet<>();
        lastpos = new HashSet<>();
    }
}
