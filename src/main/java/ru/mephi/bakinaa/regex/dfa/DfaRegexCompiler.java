package ru.mephi.bakinaa.regex.dfa;

import ru.mephi.bakinaa.GVUtils;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.tree.TreeNode;

import java.util.*;

public class DfaRegexCompiler {
    private final TreeNode root;
    private final SymbolsTable symbolsTable;

    private final Map<Set<Integer>, Integer> states = new HashMap<>();

    private DFA dfa = new DFA(0, 1, 2);

    private FollowPos followPos;
    private int lastIndex = -1;


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
        GVUtils.saveDFA(dfa, states, "3.png");

        minimizeDFA();
        GVUtils.saveDFA(dfa, "4.png");

        return new DfaRegEx(symbolsTable, dfa);
    }

    private void buildDFA() {
        int eolCharIndex = lastIndex;

        Queue<Set<Integer>> statesQueue = new ArrayDeque<>();
        statesQueue.add(root.getFirstpos());
        statesQueue.add(new HashSet<>());

        states.put(root.getFirstpos(), dfa.getInitialState());
        states.put(new HashSet<>(), dfa.getStubId());

        dfa.setFinal(dfa.getInitialState(), root.getFirstpos().contains(eolCharIndex));

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
        int toStateId;
        if (!states.containsKey(toState)) {
            states.put(toState, toStateId = dfa.addState(isFinal));
            newStateCreated = true;
        } else {
            toStateId = states.get(toState);
        }

        dfa.addTransition(states.get(fromState), toStateId, charId);

        return newStateCreated;
    }

    private void minimizeDFA() {
        dfa = new DFAMinimizer(dfa, symbolsTable).minimize();
    }


}
