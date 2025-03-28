package ru.mephi.bakinaa.lab3.lang.tree.terms;

import lombok.AllArgsConstructor;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;

@AllArgsConstructor
public class Str extends TreeNode {
    public String value;

    public static Str fromQuotedString(String str) {
        return new Str(str.substring(1, str.length() - 1));
    }
}
