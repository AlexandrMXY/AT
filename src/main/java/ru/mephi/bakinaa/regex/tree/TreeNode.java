package ru.mephi.bakinaa.regex.tree;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public abstract class TreeNode implements Cloneable {
    @Setter protected TreeNode left;
    @Setter protected TreeNode right;
    @Setter protected TreeNode parent;

    protected Set<Integer> firstpos = new HashSet<>();
    protected Set<Integer> lastpos = new HashSet<>();

    public boolean nullable;

    protected abstract void calculateOwnPos();

    public void calcPos() {
        if (left != null)
            left.calcPos();
        if (right != null)
            right.calcPos();
        calculateOwnPos();
    }

    public TreeNode copy() {
        try {
            TreeNode clone = (TreeNode) clone();
            if (left != null) {
                clone.left = left.copy();
                clone.left.parent = clone;
            }
            if (right != null) {
                clone.right = right.copy();
                clone.right.parent = clone;
            }

            clone.firstpos = new HashSet<>(firstpos);
            clone.lastpos = new HashSet<>(lastpos);

            clone.parent = null;

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
