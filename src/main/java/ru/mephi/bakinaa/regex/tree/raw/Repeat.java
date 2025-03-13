package ru.mephi.bakinaa.regex.tree.raw;

import lombok.Getter;
import ru.mephi.bakinaa.regex.tree.*;

@Getter
public class Repeat extends RawNode {
    int from;
    int to;

    public Repeat(TreeNode node, int from, int to) {
        setLeft(node);
        this.from = from;
        this.to = to;
    }

    @Override
    public TreeNode transform(TreeTransforamtionContext context) {
        TreeNode req = createRequired(from);
        TreeNode opt = to == Integer.MAX_VALUE ? new Star(operand().copy()) : createOptional(to - from);
        return new Concat(req, opt);
    }

    public TreeNode operand() {
        return getLeft();
    }

    private TreeNode createRequired(int cnt) {
        if (cnt == 0)
            return new EpsChar();
        if (cnt == 1)
            return operand().copy();

        return new Concat(createRequired(cnt / 2), createRequired(cnt / 2 + cnt % 2));
    }

    private TreeNode createOptional(int cnt) {
        if (cnt == 0)
            return new EpsChar();
        if (cnt == 1)
            return new Or(operand().copy(), new EpsChar());

        return new Concat(createOptional(cnt / 2), createOptional(cnt / 2 + cnt % 2));
    }

    @Override
    public void reverse() {}
}
