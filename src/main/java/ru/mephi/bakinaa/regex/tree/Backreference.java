package ru.mephi.bakinaa.regex.tree;

public class Backreference extends TreeNode {
    public final int captureId;

    public Backreference(int captureId) {
        this.captureId = captureId;
    }

    @Override
    protected void calculateOwnPos() {
        throw new UnsupportedOperationException();
    }
}
