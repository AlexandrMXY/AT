package ru.mephi.bakinaa.regex.tree.raw;

import lombok.AllArgsConstructor;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.tree.TreeNode;

public abstract class RawNode extends TreeNode {
    public abstract TreeNode transform(TreeTransforamtionContext context);

    @AllArgsConstructor
    public static class TreeTransforamtionContext {
        SymbolsTable symbolsTable;
    }

}
