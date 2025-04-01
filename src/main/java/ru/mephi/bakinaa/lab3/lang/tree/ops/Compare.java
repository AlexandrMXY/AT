package ru.mephi.bakinaa.lab3.lang.tree.ops;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.lang.enums.ComparisonMode;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;

@AllArgsConstructor
@Getter
public class Compare implements TreeNode {
    private ComparisonMode mode;
    private TreeNode left;
    private TreeNode right;
}
