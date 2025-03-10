package ru.mephi.bakinaa.regex.dfa;

import ru.mephi.bakinaa.GVUtils;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.tree.TreeNode;

import java.util.*;

public class DfaRegexCompiler {
    private final TreeNode root;
    private final SymbolsTable symbolsTable;

    private final Map<Set<Integer>, DFAState> states = new HashMap<>();
    private DFAState initialState = new DFAState(false, 1);
    private final DFAState stubState = new DFAState(false, 0);

    private FollowPos followPos;
    private int lastIndex = -1;

    private int nextStateIndex = 2;

    public DfaRegexCompiler(TreeNode root, SymbolsTable symbolsTable) {
        this.root = root;
        this.symbolsTable = symbolsTable;
    }

    public DfaRegEx compile() {
        System.out.println("Compiling DFA");
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

        return new DfaRegEx(symbolsTable, initialState);
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
