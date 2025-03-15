package ru.mephi.bakinaa.regex.nfa;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.IOUtils;
import ru.mephi.bakinaa.regex.RegEx;
import ru.mephi.bakinaa.regex.RegExException;
import ru.mephi.bakinaa.regex.RegExMatcher;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NfaRegEx implements RegEx {
    private final NFA nfa;
    private final SymbolsTable symbolsTable;

    @Override
    public RegExMatcher matcher(String string) {
        return new NfaRegExMatcher(nfa, symbolsTable, string);
    }

    @Override
    public String restore() {
        List<NFANode> nodes = new ArrayList<>(nfa.getStates());
        for (int i = 0; i < nfa.getStates() + 2; i++)
            nodes.add(new NFANode());

        var initialState = nodes.get(nfa.getStates());
        var finalState = nodes.get(nfa.getStates() + 1);

        for (int fromId = 0; fromId < nfa.getStates(); fromId++) {
            for (var tr : nfa.getTransitions().get(fromId)) {
                String label = switch (tr) {
                    case NFATransition.StandardTransition t -> symbolsTable.charAsString(t.getCharId());
                    case NFATransition.EpsilonTransition t -> "$";
                    default -> throw new RegExException("Unable to restore regex with capture groups");
                };
                if (tr.getTarget() == fromId) {
                    NFANode node = nodes.get(fromId);
                    node.self.add(new LabledTransition(label, node, node));
                    continue;
                }
                LabledTransition t = new LabledTransition(label, nodes.get(fromId), nodes.get(tr.getTarget()));
                nodes.get(fromId).outcoming.add(t);
                nodes.get(tr.getTarget()).incomping.add(t);
            }
        }

        LabledTransition finalTr = new LabledTransition("$", nodes.get(nfa.getFinalStateId()), finalState);
        finalState.incomping.add(finalTr);
        nodes.get(nfa.getFinalStateId()).outcoming.add(finalTr);

        LabledTransition initialTr = new LabledTransition("$", initialState, nodes.get(nfa.getInitialStateId()));
        initialState.outcoming.add(initialTr);
        nodes.get(nfa.getInitialStateId()).incomping.add(initialTr);


        IOUtils.saveNFA(nodes, "el" + ".png");
        for (int i = 0; i < nodes.size() - 2; i++) {
            eliminateState(nodes.get(i));
            IOUtils.saveNFA(nodes, "el" + i + ".png");
        }

        return initialState.outcoming.stream()
                .filter((tr) -> tr.target == finalState)
                .map((tr) -> tr.label)
                .map(l -> l.isEmpty() ? "$" : l)
                .collect(Collectors.joining("|"));
    }

    private void eliminateState(NFANode node) {
        String repeat = node.self.stream().map(t -> t.label).collect(Collectors.joining("|"));

        for (int incIndex = node.incomping.size() - 1; incIndex >= 0; incIndex--) {
            LabledTransition inc = node.incomping.get(incIndex);
            if (inc.source.eliminated) {
                node.incomping.remove(incIndex);
                continue;
            }

            for (int outIndex = node.outcoming.size() - 1; outIndex >= 0; outIndex--) {
                LabledTransition out = node.outcoming.get(outIndex);
                if (out.target.eliminated) {
                    node.outcoming.remove(outIndex);
                    continue;
                }

                addTransition(inc.source, out.target, inc.label, repeat, out.label);

            }
        }

        node.eliminated = true;
    }

    private void addTransition(NFANode from, NFANode to, String l1, String lr, String l2) {
        StringBuilder sb = new StringBuilder();
        if (!l1.isEmpty() && !l1.equals("$"))
            sb.append("(").append(l1).append(")");
        if (!lr.isEmpty() && !lr.equals("$"))
            sb.append("(").append(lr).append(")*");
        if (!l2.isEmpty() && !l2.equals("$"))
            sb.append("(").append(l2).append(")");
        LabledTransition tr = new LabledTransition(sb.toString(), from, to);
        if (from == to) {
            from.self.add(tr);
            return;
        }
        from.outcoming.add(tr);
        to.incomping.add(tr);
    }

    @Override
    public RegEx subtract(RegEx other) {
        throw new UnsupportedOperationException();
    }

    public static class NFANode {
        public boolean eliminated = false;
        public List<LabledTransition> incomping = new ArrayList<>();
        public List<LabledTransition> outcoming = new ArrayList<>();
        public List<LabledTransition> self = new ArrayList<>();
    }

    @AllArgsConstructor
    public static class LabledTransition {
        public String label;
        public NFANode source;
        public NFANode target;
    }
}
