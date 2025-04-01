package ru.mephi.bakinaa.lab3.lang.tree.ops;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;

@Getter
@AllArgsConstructor
public class Or implements TreeNode {
    private TreeNode left;
    private TreeNode right;
}
