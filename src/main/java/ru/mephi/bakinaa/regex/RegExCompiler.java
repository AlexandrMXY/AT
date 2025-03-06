package ru.mephi.bakinaa.regex;

import ru.mephi.bakinaa.GVUtils;
import ru.mephi.bakinaa.regex.chars.CharGroup;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.parser.Lexer;
import ru.mephi.bakinaa.regex.parser.Parser;
import ru.mephi.bakinaa.regex.tree.*;
import ru.mephi.bakinaa.regex.tree.raw.RawNode;

import java.io.StringReader;
import java.util.*;

/*
Инверсия - язык в котором строки рзвернуты наоборот
 */

public class RegExCompiler {
    private final String regexString;
    private TreeNode root;
    private final SymbolsTable symbolsTable = new SymbolsTable();

    private Map<Set<Integer>, DFAState> states = new HashMap<>();
    private final DFAState initialState = new DFAState(false);

    private FollowPos followPos;
    private int lastIndex = -1;

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

//        dbg();

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

        return new RegEx(symbolsTable, initialState);
    }

    private void dbg() {

        root =
                new Concat(
                        new Concat(
                                new Concat(
                                        new Star(new Char(1)),
                                        new Star(new Char(2))
                                ),
                                new Or(
                                        new Concat(
                                                new Char(3),
                                                new Star(new Char(4))
                                        ),
                                        new Char(5)
                                )
                        ),
                        new Char(6)
                );
    }

    private void buildTree() {
        Lexer lexer = new Lexer(new StringReader(regexString));
        Parser parser = new Parser(true);
        parser.lexer = lexer;
        parser.sTable = symbolsTable;

        parser.run();

        root = (TreeNode) parser.yyval().obj;
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

        List<TreeNode> newChildren = new ArrayList<>(node.getChildren().size());
        for (TreeNode child : node.getChildren()) {
            TreeNode transformed = transfromIter(child, context);
            transformed.setParent(node);
            newChildren.add(transformed);
        }
        node.setChildren(newChildren);
        return node;
    }

    private void buildDFA() {
        int eolCharIndex = lastIndex;

        Queue<Set<Integer>> statesQueue = new ArrayDeque<>();
        statesQueue.add(root.getFirstpos());
        states.put(root.getFirstpos(), initialState);

        while (!statesQueue.isEmpty()) {
            Set<Integer> stateId = statesQueue.poll();

            for(int charId : symbolsTable) {
                Set<Integer> nextStateId = new HashSet<>();

                for (int stateIndex : stateId) {
                    if (symbolsTable.getCharIdOfTreeIndex(stateIndex) == charId) {
                        nextStateId.addAll(followPos.get(stateIndex));
                    }
                }

                if (nextStateId.isEmpty())
                    continue;

                boolean isFinal = nextStateId.contains(eolCharIndex);

                boolean createdNewState = addTransition(stateId, nextStateId, charId, isFinal);
                if (createdNewState) {
                    statesQueue.add(nextStateId);
                }
            }
        }
    }

    private boolean addTransition(Set<Integer> fromState, Set<Integer> toState, int charId, boolean isFinal) {
        DFAState p = states.putIfAbsent(toState, new DFAState(isFinal));
        states.get(fromState).transitions.put(charId, states.get(toState));
        return p == null;
    }

    private void minimizeDFA() {
        // TODO
    }
}
