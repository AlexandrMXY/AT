package ru.mephi.bakinaa.lab3.commons.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Str extends SimpleObj implements TreeNode {
    public final String value;

    public static Str fromQuotedString(String str) {
        return new Str(str.substring(1, str.length() - 1));
    }


    @Override
    public String toString() {
        return value;
    }
}
