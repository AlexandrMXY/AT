package ru.mephi.bakinaa.regex;

import ru.mephi.bakinaa.IOUtils;
import ru.mephi.bakinaa.regex.chars.CharGroup;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.dfa.DfaRegexCompiler;
import ru.mephi.bakinaa.regex.nfa.NfaRegexCompiler;
import ru.mephi.bakinaa.regex.parser.Parser;
import ru.mephi.bakinaa.regex.tree.*;
import ru.mephi.bakinaa.regex.tree.raw.RawNode;

import java.util.*;


public class CommonRegExCompiler implements RegExCompiler {
    private final String regexString;
    private TreeNode root;
    private final SymbolsTable symbolsTable = new SymbolsTable();

    private final Set<Integer> capturesGroups = new HashSet<>();
    private boolean forceNfa = false;
    private boolean reversed = false;


    public CommonRegExCompiler(String regexString) {
        this.regexString = regexString;
    }

    public CommonRegExCompiler(String regexString, boolean forceNfa) {
        this(regexString);
        this.forceNfa = forceNfa;
    }
    public CommonRegExCompiler(String regexString, boolean forceNfa, boolean reversed) {
        this(regexString, forceNfa);
        this.reversed = reversed;
    }


    public RegEx compile() {
        buildTree();
        IOUtils.saveTree(root, "0.png");

        rebuildTree();
        IOUtils.saveTree(root, "1.png");

        IOUtils.println("SymTable");
        for (CharGroup g : symbolsTable.getGroups())
            IOUtils.printf("%s ", g.toString());

        IOUtils.println("\nICT");
        if (IOUtils.isPrint())
            symbolsTable.getIndexCharTable().forEach((k, v) -> {
                IOUtils.printf("%d %s\n", k, v <= 0 ? v : symbolsTable.getGroup(v));
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

        root = transfromIter(root, transformContext, true);

        root = new Concat(root, new Char(symbolsTable.nextTreeIndex(symbolsTable.eol())));
    }

    private TreeNode transfromIter(TreeNode node, RawNode.TreeTransforamtionContext context, boolean canCapture) {
        if (reversed)
            node.reverse();

        if (node instanceof RawNode rawNode) {
            node = rawNode.transform(context);
        }

        if (node instanceof Star)
            canCapture = false;

        if (!canCapture && node instanceof Capture)
            throw new RegExException("Illegal capture group usage");

        if (node.getLeft() != null) {
            TreeNode transformed = transfromIter(node.getLeft(), context, canCapture);
            transformed.setParent(node);
            node.setLeft(transformed);
        }
        if (node.getRight() != null) {
            TreeNode transformed = transfromIter(node.getRight(), context, canCapture);
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
