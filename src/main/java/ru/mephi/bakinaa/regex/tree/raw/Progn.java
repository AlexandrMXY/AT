package ru.mephi.bakinaa.regex.tree.raw;

import ru.mephi.bakinaa.regex.RegExException;
import ru.mephi.bakinaa.regex.tree.Concat;
import ru.mephi.bakinaa.regex.tree.TreeNode;

public class Progn extends RawNode {
    public Progn(TreeNode left, TreeNode right) {
        setLeft(left);
        setRight(right);
    }

    @Override
    public TreeNode transform(TreeTransforamtionContext context) {
        if (getParent() != null)
            throw new RegExException("Illegal \"/\" operator useage");
        return new Concat(getLeft(), getRight());
    }

    @Override
    public void reverse() {}
}
