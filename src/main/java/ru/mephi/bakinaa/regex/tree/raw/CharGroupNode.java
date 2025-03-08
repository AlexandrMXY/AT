package ru.mephi.bakinaa.regex.tree.raw;

import ru.mephi.bakinaa.regex.chars.CharGroup;
import ru.mephi.bakinaa.regex.tree.Char;
import ru.mephi.bakinaa.regex.tree.EpsChar;
import ru.mephi.bakinaa.regex.tree.Or;
import ru.mephi.bakinaa.regex.tree.TreeNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CharGroupNode extends RawNode {
    public final List<CharGroup> groups;

    public CharGroupNode(List<CharGroup> groups) {
        this.groups = groups;
    }

    @Override
    public TreeNode transform(TreeTransforamtionContext context) {
        Set<Integer> uniqe = new HashSet<>();

        for (CharGroup g : groups)
            for (char c : g)
                uniqe.add(context.symbolsTable.idOf(c));

        return uniqe.stream()
                .map((id) -> (TreeNode) new Char(context.symbolsTable.nextTreeIndex(id)))
                .reduce(Or::new)
                .orElseGet(EpsChar::new);
    }
}
