package ru.mephi.bakinaa.lab3.commons;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Id implements TreeNode {
    public String scope = null;
    public String value;

    public Id(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return scope == null ? value : scope + "::" + value;
    }
}
