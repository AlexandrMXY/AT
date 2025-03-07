package ru.mephi.bakinaa.regex;

import ru.mephi.bakinaa.regex.chars.SymbolsTable;

import java.util.*;
import java.util.function.Consumer;


public class DFAMinimizer {
    public static final int INITIAL_STATE_ID = 1;
    private final Map<Set<Integer>, DFAState> statesMap;
    private final SymbolsTable symbolsTable;

    private int statesCnt;
    private DFAState[] states;
    // invEdges[targetEdgeId].get(charId) -> sourceEdgeIds
    private List<Map<Integer, List<Integer>>> invEdges;
    private BitSet marked;

    public DFAMinimizer(Map<Set<Integer>, DFAState> statesMap, SymbolsTable symbolsTable) {
        this.statesMap = statesMap;
        this.symbolsTable = symbolsTable;
        init();
    }

    private void init() {
        statesCnt = statesMap.size();
        states = new DFAState[statesCnt];
        invEdges = new ArrayList<>(statesCnt);
        marked = new BitSet(statesCnt * statesCnt);

        statesMap.forEach((k, v) -> states[v.index] = v);

        for (int i = 0; i < statesCnt; i++)
            invEdges.add(new HashMap<>());

        for (DFAState state : states) {
            state.transitions.forEach((charId, nextState) -> {
                invEdges.get(nextState.index).computeIfAbsent(charId, ArrayList::new);
                invEdges.get(nextState.index).get(charId).add(state.index);
            });
        }


    }

    public DFAState minimize() {
        Queue<Pair> queue = new ArrayDeque<>();

        for (int i = 0; i < statesCnt; i++) {
            for (int j = 0; j < statesCnt; j++) {
                if (!isMarked(i, j) && (states[i].isFinal != states[j].isFinal)) {
                    setMarked(i, j);
                    setMarked(j, i);
                    queue.add(new Pair(i, j));
                }
            }
        }


        while (!queue.isEmpty()) {
            Pair p = queue.poll();
            for (int charId : symbolsTable) {
                iterateReversTransitions(p.a, charId, (s) -> {
                    iterateReversTransitions(p.b, charId, (t) -> {
                        if (!isMarked(s, t)) {
                            setMarked(s, t);
                            setMarked(t, s);
                            queue.add(new Pair(s, t));
                        }
                    });
                });
            }
        }

        int[] components = new int[statesCnt];
        int nextComponentIndex = 1;

        for (int i = 0; i < statesCnt; i++) {
            if (components[i] == 0) {
                components[i] = nextComponentIndex++;
                for (int j = i + 1; j < statesCnt; j++) {
                    if (!isMarked(i, j))
                        components[j] = components[i];
                }
            }
        }

        System.out.println("\nComponents");
        System.out.println(Arrays.toString(components));

        System.out.println("\nMarked");
        for (int i = 0; i < statesCnt; i++) {
            for (int j = 0; j < statesCnt; j++) {
                System.out.print(isMarked(i, j) ? '1' : '0');
            }
            System.out.println();
        }

        return buildDFA(components, nextComponentIndex - 1);
    }

    private DFAState buildDFA(int[] components, int componenstsCnt) {
        DFAState[] resultStates = new DFAState[componenstsCnt];
        for (int i = 0; i < componenstsCnt; i++)
            resultStates[i] = new DFAState(i);

        for (int i = 0; i < components.length; i++) {
            int finalI = i;
            states[i].transitions.forEach((charId, nextState) -> {
                resultStates[components[finalI] - 1].transitions
                        .put(charId, resultStates[components[nextState.index] - 1]);
            });

            if (states[i].isFinal) {
                resultStates[components[i] - 1].isFinal = true;
            }
        }

        return resultStates[components[INITIAL_STATE_ID] - 1];
    }

    private void iterateReversTransitions(int to, int charId, Consumer<Integer> fn) {
        var sources = invEdges.get(to).get(charId);
        if (sources != null)
            sources.forEach(fn);
    }

    private boolean isMarked(int a, int b) {
        return marked.get(a * statesCnt + b);
    }

    private void setMarked(int a, int b) {
        marked.set(a * statesCnt + b, true);
    }


    private record Pair(
            int a,
            int b
    ) {}
}
