package ru.mephi.bakinaa.regex.tree;

import ru.mephi.bakinaa.regex.RegExException;

public class Capture extends TreeNode {
    public final int id;

    public Capture(TreeNode node, int id) {
        setLeft(node);
        this.id = id;
    }

    @Override
    public TreeNode copy() {
        throw new RegExException("Unnable to use + or {,} with an expression that includes a capture group");
    }

    @Override
    protected void calculateOwnPos() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reverse() {}
}
