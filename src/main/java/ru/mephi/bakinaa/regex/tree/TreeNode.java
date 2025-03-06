package ru.mephi.bakinaa.regex.tree;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public abstract class TreeNode {
    @Setter protected List<TreeNode> children = new ArrayList<>();
    @Setter protected TreeNode parent;

    protected Set<Integer> firstpos = new HashSet<>();
    protected Set<Integer> lastpos = new HashSet<>();

    public boolean nullable;

    public void addChild(TreeNode child) {
        if (child == null)
            return;
        if (child.parent == this)
            return;
        if (child.parent != null)
            child.parent.removeChild(child);

        children.add(child);
        child.parent = this;
    }

    public void removeChild(TreeNode child) {
        if (child.parent != this)
            return;

        children.remove(child);
        child.parent = null;
    }


    protected abstract void calculateOwnPos();

    public void calcPos() {
        for (TreeNode child : children)
            child.calcPos();
        calculateOwnPos();
    }
}
