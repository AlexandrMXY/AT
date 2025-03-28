package ru.mephi.bakinaa.lab3.lang.tree.terms;

import lombok.AllArgsConstructor;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;

@AllArgsConstructor
public class FPNum extends TreeNode {
    public double value;

    public FPNum(String value) {
        this.value = Double.parseDouble(value);
    }
}
