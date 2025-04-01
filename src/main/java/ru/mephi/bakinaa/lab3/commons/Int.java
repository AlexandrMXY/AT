package ru.mephi.bakinaa.lab3.commons;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Int extends SimpleObj implements TreeNode {
    public final long value;

    public static Int parse(String str) {
        return new Int(Long.parseLong(str));
    }
}
