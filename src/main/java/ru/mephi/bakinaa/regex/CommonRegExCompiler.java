package ru.mephi.bakinaa.regex;

import ru.mephi.bakinaa.GVUtils;
import ru.mephi.bakinaa.regex.chars.CharGroup;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.dfa.DfaRegEx;
import ru.mephi.bakinaa.regex.dfa.DfaRegexCompiler;
import ru.mephi.bakinaa.regex.nfa.NfaRegexCompiler;
import ru.mephi.bakinaa.regex.parser.Parser;
import ru.mephi.bakinaa.regex.tree.*;
import ru.mephi.bakinaa.regex.tree.raw.RawNode;

import java.util.*;

/*
Инверсия - язык в котором строки рзвернуты наоборот
 */

public class CommonRegExCompiler implements RegExCompiler {
    private final String regexString;
    private TreeNode root;
    private final SymbolsTable symbolsTable = new SymbolsTable();

    private final Set<Integer> capturesGroups = new HashSet<>();
    private boolean forceNfa = false;


    public CommonRegExCompiler(String regexString) {
        this.regexString = regexString;
    }

    public CommonRegExCompiler(String regexString, boolean forceNfa) {
        this(regexString);
        this.forceNfa = forceNfa;
    }

    public RegEx compile() {
        buildTree();
        GVUtils.saveTree(root, "0.png");

        rebuildTree();
        GVUtils.saveTree(root, "1.png");

        System.out.println("SymTable");
        for (CharGroup g : symbolsTable.getGroups())
            System.out.printf("%s ", g.toString());

        System.out.println("\nICT");
        symbolsTable.getIndexCharTable().forEach((k, v) -> {
            System.out.printf("%d %s\n", k, v <= 0 ? v : symbolsTable.getGroup(v));
        });

        if (!capturesGroups.isEmpty() || forceNfa)
            return new NfaRegexCompiler(root, symbolsTable).compile();
        else
            return new DfaRegexCompiler(root, symbolsTable).compile();
    }

    private void buildTree() {
        root = new Parser(regexString, symbolsTable).buildTree();
    }

    private void rebuildTree() {
        var transformContext = new RawNode.TreeTransforamtionContext(symbolsTable);

        root = transfromIter(root, transformContext);

        root = new Concat(root, new Char(symbolsTable.nextTreeIndex(symbolsTable.eol())));
    }

    private TreeNode transfromIter(TreeNode node, RawNode.TreeTransforamtionContext context) {
        if (node instanceof RawNode rawNode) {
            node = rawNode.transform(context);
        }

        if (node.getLeft() != null) {
            TreeNode transformed = transfromIter(node.getLeft(), context);
            transformed.setParent(node);
            node.setLeft(transformed);
        }
        if (node.getRight() != null) {
            TreeNode transformed = transfromIter(node.getRight(), context);
            transformed.setParent(node);
            node.setRight(transformed);
        }

        if (node instanceof Capture cap) {
            capturesGroups.add(cap.id);
        }
        if (node instanceof Backreference backreference) {
            if (!capturesGroups.contains(backreference.captureId))
                throw new RegExException("Backreference to unknown capture group");
        }

        return node;
    }

}
