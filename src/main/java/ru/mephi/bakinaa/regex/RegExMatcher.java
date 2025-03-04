package ru.mephi.bakinaa.regex;

import ru.mephi.bakinaa.regex.chars.SymbolsTable;

public class RegExMatcher {
    private final RegexDFA.State initState;
    private final SymbolsTable symbolsTable;

    public RegExMatcher(RegexDFA.State initState, SymbolsTable symbolsTable) {
        this.initState = initState;
        this.symbolsTable = symbolsTable;
    }

    public boolean matches(String string) {
        RegexDFA.State state = initState;

        for (char c : string.toCharArray()) {
            int cId = symbolsTable.idOf(c);
            if (cId == SymbolsTable.UNKNOWN_CHAR)
                return false;
            state = state.transitions.get(cId);
            if (state == null)
                return false;
        }

        return state.isFinal;
    }
}
