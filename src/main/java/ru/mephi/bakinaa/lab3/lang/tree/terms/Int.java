package ru.mephi.bakinaa.lab3.lang.tree.terms;

import lombok.AllArgsConstructor;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;

@AllArgsConstructor
public class Int extends TreeNode {
    public long value;

    public Int(String value) {
        this.value = Long.parseLong(value);
    }
}
