package ru.mephi.bakinaa.regex;

import ru.mephi.bakinaa.GVUtils;
import ru.mephi.bakinaa.regex.chars.CharGroup;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.parser.Parser;
import ru.mephi.bakinaa.regex.tree.*;
import ru.mephi.bakinaa.regex.tree.raw.RawNode;

import java.util.*;

/*
Инверсия - язык в котором строки рзвернуты наоборот
 */

public class RegExCompiler {
    private final String regexString;
    private TreeNode root;
    private final SymbolsTable symbolsTable = new SymbolsTable();

    private Map<Set<Integer>, DFAState> states = new HashMap<>();
    private DFAState initialState = new DFAState(false, 1);
    private DFAState stubState = new DFAState(false, 0);

    private FollowPos followPos;
    private int lastIndex = -1;

    private int nextStateIndex = 2;

    public RegExCompiler(String regexString) {
        this.regexString = regexString;
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

        lastIndex = symbolsTable.getLastTreeIndex();
        root.calcPos();
        followPos = FollowPos.forTree(root, lastIndex);

        GVUtils.saveTree(root, "2.png", GVUtils::datalizedNodeLabel);
        System.out.println("\nfollowpos");
        for (int i = 0; i < lastIndex; i++)
            System.out.printf("%d %s\n", i, followPos.get(i).toString());

        buildDFA();
        GVUtils.saveDFA(initialState, states, "3.png");

        minimizeDFA();
        GVUtils.saveDFA(initialState, "4.png");

        return new RegEx(symbolsTable, initialState);
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

        return node;
    }

    private void buildDFA() {
        int eolCharIndex = lastIndex;

        Queue<Set<Integer>> statesQueue = new ArrayDeque<>();
        statesQueue.add(root.getFirstpos());
        states.put(root.getFirstpos(), initialState);
        states.put(new HashSet<>(), stubState);

        for (int charId : symbolsTable) {
            stubState.transitions.put(charId, stubState);
        }

        while (!statesQueue.isEmpty()) {
            Set<Integer> stateId = statesQueue.poll();

            for(int charId : symbolsTable) {
                Set<Integer> nextStateId = new HashSet<>();

                for (int stateIndex : stateId) {
                    if (symbolsTable.getCharIdOfTreeIndex(stateIndex) == charId) {
                        nextStateId.addAll(followPos.get(stateIndex));
                    }
                }

                boolean isFinal = nextStateId.contains(eolCharIndex);

                boolean createdNewState = addTransition(stateId, nextStateId, charId, isFinal);
                if (createdNewState) {
                    statesQueue.add(nextStateId);
                }
            }
        }
    }

    private boolean addTransition(Set<Integer> fromState, Set<Integer> toState, int charId, boolean isFinal) {
        boolean newStateCreated = false;
        if (!states.containsKey(toState)) {
            states.put(toState, new DFAState(isFinal, nextStateIndex++));
            newStateCreated = true;
        }

        states.get(fromState).transitions.put(charId, states.get(toState));
        return newStateCreated;
    }

    private void minimizeDFA() {
        initialState = new DFAMinimizer(states, symbolsTable).minimize();
    }


}
