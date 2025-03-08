package ru.mephi.bakinaa.regex;

import ru.mephi.bakinaa.regex.tree.Concat;
import ru.mephi.bakinaa.regex.tree.Star;
import ru.mephi.bakinaa.regex.tree.TreeNode;

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
        if (node == null)
            return;

        if (node instanceof Concat concat) {
            for (int i : concat.getLeft().getLastpos()) {
                followposMap.get(i).addAll(concat.getRight().getFirstpos());
            }
        }
        if (node instanceof Star star) {
            for (int i : star.getChild().getLastpos()) {
                followposMap.get(i).addAll(star.getChild().getFirstpos());
            }
        }

        iterate(node.getLeft());
        iterate(node.getRight());
    }
}
