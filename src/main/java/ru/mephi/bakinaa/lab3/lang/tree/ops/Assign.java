package ru.mephi.bakinaa.lab3.lang.tree.ops;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;
import ru.mephi.bakinaa.lab3.lang.tree.terms.Id;

@AllArgsConstructor
@Getter
public class Assign extends TreeNode {
    private Id left;
    private TreeNode right;
}
