package ru.mephi.bakinaa.regex.tree.raw;

import lombok.Getter;
import ru.mephi.bakinaa.regex.tree.TreeNode;

public class Repeat extends RawNode {
    @Getter int from;
    @Getter int to;

    public Repeat(TreeNode node, int from, int to) {
        setLeft(node);
    }

    public static Repeat fromString(TreeNode node, String str) {
        // TODO
        return new Repeat(node, 0, 1);
    }

    @Override
    public TreeNode transform(TreeTransforamtionContext context) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
