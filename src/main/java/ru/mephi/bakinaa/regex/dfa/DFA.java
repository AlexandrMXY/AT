package ru.mephi.bakinaa.regex.dfa;

import lombok.Getter;

import java.util.*;

public class DFA {
    // transitions.get(fromId).get(charId) = toId
    @Getter private List<Map<Integer, Integer>> transitions = new ArrayList<>();
    @Getter private Set<Integer> finals = new HashSet<>();
    @Getter private int states = 0;

    @Getter private int initialState;
    @Getter private int stubId;

    public DFA(int initialState, int stubId, int statesCnt) {
        this.initialState = initialState;
        this.stubId = stubId;
        for (int i = 0; i < statesCnt; i++)
            addState();

    }

    public int addState() {
        return addState(false);
    }

    public int addState(boolean isFinal) {
        transitions.add(new HashMap<>());
        if (isFinal)
            finals.add(states);
        return states++;
    }

    public void addTransition(int from, int to, int charId) {
        transitions.get(from).put(charId, to);
    }

    public boolean isFinal(int id) {
        return finals.contains(id);
    }

    public void setFinal(int id, boolean value) {
        if (value)
            finals.add(id);
        else
            finals.remove(id);
    }

    public int move(int from, int charId) {
        Integer i = transitions.get(from).get(charId);
        return i == null ? -1 : i;
    }
}
