package ru.mephi.bakinaa.regex;

import java.util.HashMap;
import java.util.Map;

public class DFAState {
    public Map<Integer, DFAState> transitions = new HashMap<>();
    public boolean isFinal = false;
    public final int index;

    public DFAState(boolean isFinal, int index) {
        this(index);
        this.isFinal = isFinal;
    }
    public DFAState(int index) {
        this.index = index;
    }
}
