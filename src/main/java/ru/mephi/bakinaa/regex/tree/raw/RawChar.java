package ru.mephi.bakinaa.regex.tree.raw;

public class RawChar extends RawNode {
    public final char c;

    public RawChar(char c) {
        this.c = c;
    }

    @Override
    public void transform(TreeTransforamtionContext context) {
        // TODO
    }

    @Override
    protected void calculatePos() { }
}
