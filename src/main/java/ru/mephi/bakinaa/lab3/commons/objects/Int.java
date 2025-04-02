package ru.mephi.bakinaa.lab3.commons.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;

@AllArgsConstructor
@EqualsAndHashCode
public class Int extends SimpleObj implements TreeNode {
    public final long value;

    public static Int parse(String str) {
        return new Int(Long.parseLong(str));
    }


    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
