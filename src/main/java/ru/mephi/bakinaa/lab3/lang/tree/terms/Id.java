package ru.mephi.bakinaa.lab3.lang.tree.terms;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Id extends TreeNode {
    public String scope = null;
    public String value;

    public Id(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
