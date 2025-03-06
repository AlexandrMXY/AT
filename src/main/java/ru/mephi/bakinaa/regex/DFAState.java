package ru.mephi.bakinaa.regex;

import java.util.HashMap;
import java.util.Map;

public class DFAState {
    public Map<Integer, DFAState> transitions = new HashMap<>();
    public boolean isFinal = false;

    public DFAState(boolean isFinal) {
        this.isFinal = isFinal;
    }
    public DFAState() {}
}
