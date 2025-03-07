package ru.mephi.bakinaa.regex.tree.raw;

import ru.mephi.bakinaa.regex.tree.TreeNode;

public class CharGroupNode extends RawNode {
    public final String groupText;

    public CharGroupNode(String groupText) {
        this.groupText = groupText;
    }

    @Override
    public TreeNode transform(TreeTransforamtionContext context) {
        // TODO
        throw new UnsupportedOperationException("Not impimented yet");
    }
}
