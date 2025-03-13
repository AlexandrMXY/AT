package ru.mephi.bakinaa.regex.tree;

public class Char extends TreeNode {
    public final int pos;

    public Char(int pos) {
        this.pos = pos;
        nullable = false;
        firstpos.add(pos);
        lastpos.add(pos);
    }


    @Override
    protected void calculateOwnPos() {}

    @Override
    public void reverse() {}
}
