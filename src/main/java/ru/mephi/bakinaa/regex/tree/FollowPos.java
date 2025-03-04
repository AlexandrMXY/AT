package ru.mephi.bakinaa.regex.tree;

import java.util.*;

public class FollowPos {
    private final Map<Integer, Set<Integer>> followposMap = new HashMap<>();

    public FollowPos(int lastNumber) {
        for (int i = 0; i < lastNumber; i++) {
            followposMap.put(i, new HashSet<>(lastNumber));
        }
    }

    public static FollowPos forTree(TreeNode root, int lastNumber) {
        var res = new FollowPos(lastNumber);
        res.iterate(root);
        return res;
    }

    public Set<Integer> get(int index) {
        return followposMap.get(index);
    }

    private void iterate(TreeNode node) {
        if (node instanceof Concat concat) {
            for (int i : concat.left().lastpos) {
                followposMap.get(i).addAll(concat.right().firstpos);
            }
        }
        if (node instanceof Star star) {
            for (int i : star.getChild().lastpos) {
                followposMap.get(i).addAll(star.getChild().firstpos);
            }
        }

        for (TreeNode child : node.children) {
            iterate(child);
        }
    }
}
