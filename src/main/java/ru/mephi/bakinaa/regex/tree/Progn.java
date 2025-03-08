package ru.mephi.bakinaa.regex.tree;

public class Progn extends TreeNode {
    public Progn(TreeNode left, TreeNode right) {
        setLeft(left);
        setRight(right);
    }

    @Override
    protected void calculateOwnPos() {
        throw new UnsupportedOperationException();
    }
}
