package ru.mephi.bakinaa.lab3.commons.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;

@AllArgsConstructor
@EqualsAndHashCode
public class Real extends SimpleObj implements TreeNode {
    public final double value;

    public static Real parse(String str) {
        return new Real(Double.parseDouble(str));
    }


    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
