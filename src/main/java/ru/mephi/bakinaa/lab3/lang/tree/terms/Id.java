package ru.mephi.bakinaa.lab3.lang.tree.terms;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;

@AllArgsConstructor
@EqualsAndHashCode
public class Id extends TreeNode {
    public String value;

    @Override
    public String toString() {
        return value;
    }
}
