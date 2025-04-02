package ru.mephi.bakinaa.lab3.commons.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;

@AllArgsConstructor
@EqualsAndHashCode
public class Bool extends SimpleObj implements TreeNode {
    public static final Bool TRUE = new Bool(true);
    public static final Bool FALSE = new Bool(false);

    public final boolean value;

    public static Bool of(boolean value) {
        return value ? TRUE : FALSE;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
