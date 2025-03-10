package ru.mephi.bakinaa.regex.nfa;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

@Getter
public class NFA {
    private int states = 0;
    private final List<List<NFATransition>> transitions = new ArrayList<>();
    @Setter
    private int finalStateId = -1;
    @Setter
    private int initialStateId = -1;

    public int addNode() {
        int nodeId = states++;
        transitions.add(new ArrayList<>());
        return nodeId;
    }

    public void addTransition(int from, int to, int charId) {
        transitions.get(from).add(new NFATransition.StandardTransition(to, charId));
    }

    public void addEpsiolnTransition(int from, int to) {
        transitions.get(from).add(new NFATransition.EpsilonTransition(to));

    }

    public void addTransition(int from, NFATransition transition) {
        transitions.get(from).add(transition);
    }

    public record StatesPair(
            int initialState,
            int finalState) {}

}
