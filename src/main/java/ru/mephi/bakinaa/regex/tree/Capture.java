package ru.mephi.bakinaa.regex.tree;

public class Capture extends TreeNode {
    public final int id;

    public Capture(TreeNode node, int id) {
        setLeft(node);
        this.id = id;
    }

    @Override
    protected void calculateOwnPos() {
        throw new UnsupportedOperationException();
    }
}
