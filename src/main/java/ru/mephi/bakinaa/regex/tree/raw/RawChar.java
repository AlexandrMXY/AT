package ru.mephi.bakinaa.regex.tree.raw;

import ru.mephi.bakinaa.regex.tree.Char;
import ru.mephi.bakinaa.regex.tree.TreeNode;

public class RawChar extends RawNode {
    public final char c;

    public RawChar(char c) {
        this.c = c;
    }
    public RawChar(String c) {
        if (c.length() > 1)
            throw new IllegalArgumentException();
        this.c = c.charAt(0);
    }

    @Override
    public TreeNode transform(TreeTransforamtionContext context) {
        return new Char(context.symbolsTable.nextTreeIndex(context.symbolsTable.idOf(c)));
    }

    @Override
    protected void calculateOwnPos() { }
}
