package ru.mephi.bakinaa.regex.dfa;

import ru.mephi.bakinaa.IOUtils;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;

import java.util.*;
import java.util.function.Consumer;


public class DFAMinimizer {
//    private final Map<Set<Integer>, DFAState> statesMap;
    private final SymbolsTable symbolsTable;

    private final int statesCnt;
//    private DFAState[] states;
    // invEdges[targetEdgeId].get(charId) -> sourceEdgeIds
    private final List<Map<Integer, List<Integer>>> invEdges;
    private final BitSet marked;
    private final DFA dfa;

    private final List<Boolean> reachable;
//    public DFAMinimizer(Map<Set<Integer>, DFAState> statesMap, SymbolsTable symbolsTable) {
//        this.statesMap = statesMap;
//        this.symbolsTable = symbolsTable;
//        init();
//    }

    public DFAMinimizer(DFA dfa, SymbolsTable symbolsTable) {
        this(dfa, symbolsTable, null);
    }

    public DFAMinimizer(DFA dfa, SymbolsTable symbolsTable, List<Boolean> reachable) {
        this.dfa = dfa;
        this.symbolsTable = symbolsTable;
        this.reachable = reachable;
        statesCnt = dfa.getStates();
        invEdges = new ArrayList<>(statesCnt);
        marked = new BitSet(statesCnt * statesCnt);

        for (int i = 0; i < statesCnt; i++)
            invEdges.add(new HashMap<>());
        for (int i = 0; i < statesCnt; i++) {
            int fromId = i;
            dfa.getTransitions().get(i).forEach((charId, toId) -> {
                invEdges.get(toId).computeIfAbsent(charId, ArrayList::new);
                invEdges.get(toId).get(charId).add(fromId);
            });
        }
    }

    public DFA minimize() {
        Queue<Pair> queue = new ArrayDeque<>();

        for (int i = 0; i < statesCnt; i++) {
            for (int j = 0; j < statesCnt; j++) {
                if (!isMarked(i, j) && (dfa.isFinal(i) != dfa.isFinal(j))) {
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
            if (reachable != null && !reachable.get(i))
                continue;
            if (components[i] == 0) {
                components[i] = nextComponentIndex++;
                for (int j = i + 1; j < statesCnt; j++) {
                    if (!isMarked(i, j))
                        components[j] = components[i];
                }
            }
        }

        IOUtils.println("\nComponents");
        IOUtils.println(Arrays.toString(components));

        IOUtils.println("\nMarked");
        if (IOUtils.isPrint())
            for (int i = 0; i < statesCnt; i++) {
                for (int j = 0; j < statesCnt; j++) {
                    IOUtils.print(isMarked(i, j) ? '1' : '0');
                }
                IOUtils.println();
            }

        return buildDFA(components, nextComponentIndex - 1);
    }

    private DFA buildDFA(int[] components, int componenstsCnt) {
        DFA result = new DFA(
                components[dfa.getInitialState()] - 1,
                components[dfa.getStubId()] - 1,
                componenstsCnt);

        for (int i = 0; i < components.length; i++) {
            if (reachable != null && !reachable.get(i))
                continue;

            int fromId = i;
            dfa.getTransitions().get(i).forEach((charId, toId) -> {
                result.addTransition(components[fromId] - 1, components[toId] - 1, charId);
            });

            if (dfa.isFinal(i))
                result.setFinal(components[i] - 1, true);
        }

        return result;
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
