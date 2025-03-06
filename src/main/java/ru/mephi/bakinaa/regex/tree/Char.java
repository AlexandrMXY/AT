package ru.mephi.bakinaa.regex.tree;

public class Char extends TreeNode {
    public Char(int pos) {
        nullable = false;
        firstpos.add(pos);
        lastpos.add(pos);
    }


    @Override
    protected void calculateOwnPos() {}
}
