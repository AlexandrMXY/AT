package ru.mephi.bakinaa.lab3.lang.tree;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Statements extends TreeNode {
    private final List<TreeNode> statements = new ArrayList<>();

    public Statements(TreeNode n) {
        statements.add(n);
    }

    public Statements add(TreeNode node) {
        if (node instanceof Statements s)
            statements.addAll(s.statements);
        else
            statements.add(node);
        return this;
    }

    public static Statements combine(TreeNode left, TreeNode right) {
        if (left instanceof Statements s)
            return s.add(right);
        Statements s = new Statements(left);
        s.add(right);
        return s;
    }
}
