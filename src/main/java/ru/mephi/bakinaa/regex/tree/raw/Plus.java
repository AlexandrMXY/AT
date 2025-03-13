package ru.mephi.bakinaa.regex.tree.raw;

import ru.mephi.bakinaa.regex.tree.Concat;
import ru.mephi.bakinaa.regex.tree.Star;
import ru.mephi.bakinaa.regex.tree.TreeNode;

public class Plus extends RawNode {
    public Plus(TreeNode operand) {
        setLeft(operand);
    }

    @Override
    public TreeNode transform(TreeTransforamtionContext context) {
        return new Concat(left.copy(), new Star(left));
    }

    @Override
    public void reverse() {}
}
