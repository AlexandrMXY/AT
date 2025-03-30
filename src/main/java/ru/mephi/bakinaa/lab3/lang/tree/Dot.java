package ru.mephi.bakinaa.lab3.lang.tree;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.lang.tree.terms.Id;

@AllArgsConstructor
@Getter
public class Dot extends TreeNode {
    private TreeNode base;
    private Id path;

    public static Dot tryCreate(TreeNode base, TreeNode path) {
        if (path instanceof Id id)
            return new Dot(base, id);
        throw new LangException("Invalid . usage");
    }
}
