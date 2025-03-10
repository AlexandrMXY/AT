package ru.mephi.bakinaa.regex.nfa;

import ru.mephi.bakinaa.regex.RegExMatcher;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class NfaRegExMatcher implements RegExMatcher {
    private final NFA nfa;
    private final StringHolder str;
    private final CaptureBuffer captureBuffer = new CaptureBuffer();

    private final Stack<Integer> transitionsIds = new Stack<>();
    private final Stack<Integer> states = new Stack<>();
    private final Stack<NFATransition> transitions = new Stack<>();

    public NfaRegExMatcher(NFA nfa, SymbolsTable symbolsTable, String string) {
        this.nfa = nfa;
        this.str = new StringHolder(symbolsTable, string);
        transitionsIds.push(0);
        states.push(nfa.getInitialStateId());
    }

    @Override
    public boolean matches() {
        while (!states.empty()) {
            if (states.peek() == nfa.getFinalStateId()) {
                return true;
            }
            step();
        }
        return false;
    }

    public Map<Integer, String> getCaptures() {
        Map<Integer, String> res = new HashMap<>();
        captureBuffer.getGroups().forEach((id, gr) -> {
            res.put(id, str.substring(captureBuffer.getCaptured(id)));
        });
        return res;
    }

    private void step() {
        int trId = transitionsIds.pop();
        var trList = nfa.getTransitions().get(states.peek());
        int maxId = trList.size();

        if (trId >= maxId) {
            backstep();
            return;
        }

        NFATransition tr;
        while (!(tr = trList.get(trId++)).move(str, captureBuffer)) {
            if (trId >= maxId) {
                backstep();
                return;
            }
        }

        states.push(trList.get(trId - 1).getTarget());
        transitions.push(tr);
        transitionsIds.push(trId);
        transitionsIds.push(0);

        if (states.toString().equals("[8, 0, 1, 2, 3, 4, 5, 6, 7, 9, 18, 10, 11, 12, 13, 14, 15, 16, 17, 19, 20]"))
            System.out.println("Here");

        System.out.println(states);
        System.out.println(captureBuffer);
        System.out.println(str.getIndex() + " " + getCaptures());
    }

    private void backstep() {
        states.pop();
        if (!transitions.empty())
            transitions.pop().backstep(str, captureBuffer);
    }
}
