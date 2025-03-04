package ru.mephi.bakinaa.regex.tree.raw;

public class CharGroupNode extends RawNode {
    public final String groupText;

    public CharGroupNode(String groupText) {
        this.groupText = groupText;
    }

    @Override
    protected void calculatePos() {  }

    @Override
    public void transform(TreeTransforamtionContext context) {
        // TODO
    }
}
