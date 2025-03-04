package ru.mephi.bakinaa.regex.tree;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public abstract class TreeNode {
    protected List<TreeNode> children = new ArrayList<>();
    protected TreeNode parent;

    protected Set<Integer> firstpos = new HashSet<>();
    protected Set<Integer> lastpos = new HashSet<>();

    public boolean nullable;

    protected void addChild(TreeNode child) {
        if (child == null)
            return;
        if (child.parent == this)
            return;
        if (child.parent != null)
            child.parent.removeChild(child);

        children.add(child);
        child.parent = this;
    }

    protected void removeChild(TreeNode child) {
        if (child.parent != this)
            return;

        children.remove(child);
        child.parent = null;
    }

    protected abstract void calculatePos();
}
