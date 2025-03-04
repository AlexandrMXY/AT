package ru.mephi.bakinaa.regex;

import lombok.Getter;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.tree.FollowPos;
import ru.mephi.bakinaa.regex.tree.TreeNode;

import java.util.*;

@Getter
public class RegexDFA {
    private Map<Set<Integer>, State> states = new HashMap<>();
    private final State initialState = new State(false);

    private RegexDFA() {}

    public static RegexDFA compile(
            TreeNode root,
            SymbolsTable symbolsTable,
            Map<Integer, Integer> indexCharTable,
            int lastIndex) {
        int eolCharIndex = lastIndex;

        FollowPos followPos = FollowPos.forTree(root, lastIndex);

        RegexDFA dfa = new RegexDFA();

        Queue<Set<Integer>> statesQueue = new ArrayDeque<>();
        statesQueue.add(root.getFirstpos());
        dfa.states.put(root.getFirstpos(), dfa.initialState);

        while (!statesQueue.isEmpty()) {
            Set<Integer> stateId = statesQueue.poll();

            for(int charId : symbolsTable) {
                Set<Integer> nextStateId = new HashSet<>();

                for (int stateIndex : stateId) {
                    if (indexCharTable.get(stateIndex) == charId) {
                        nextStateId.addAll(followPos.get(stateIndex));
                    }
                }

                if (nextStateId.isEmpty())
                    continue;

                boolean isFinal = nextStateId.contains(eolCharIndex);

                boolean createdNewState = dfa.addTransition(stateId, nextStateId, charId, isFinal);
                if (createdNewState) {
                    statesQueue.add(nextStateId);
                }
            }
        }

        return dfa;
    }


    // return true if created new state
    private boolean addTransition(Set<Integer> fromState, Set<Integer> toState, int charId, boolean isFinal) {
        State p = states.putIfAbsent(toState, new State(isFinal));
        states.get(fromState).transitions.put(charId, states.get(toState));
        return p == null;
    }

    public static class State {
        public Map<Integer, State> transitions = new HashMap<>();
        public boolean isFinal = false;

        public State(boolean isFinal) {
            this.isFinal = isFinal;
        }
    }
}
