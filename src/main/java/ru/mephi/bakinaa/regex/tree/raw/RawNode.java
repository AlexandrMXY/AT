package ru.mephi.bakinaa.regex.tree.raw;

import ru.mephi.bakinaa.regex.tree.TreeNode;

public abstract class RawNode extends TreeNode {
    public abstract void transform(TreeTransforamtionContext context);

    public static class TreeTransforamtionContext {

    }
}
