package ru.mephi.bakinaa.lab3.commons;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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

}
