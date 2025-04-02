package ru.mephi.bakinaa.lab3.lang.tree.ops;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

@AllArgsConstructor
@Getter
public class Assign implements TreeNode {
    private Id left;
    private TreeNode right;
}
