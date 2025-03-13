package ru.mephi.bakinaa.regex.dfa;

import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.regex.RegExMatcher;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;

@RequiredArgsConstructor
public class DfaRegExMatcher implements RegExMatcher {
    private final DFA dfa;
    private final SymbolsTable symbolsTable;
    private final String string;

    @Override
    public boolean matches() {
        int state = dfa.getInitialState();

        for (char c : string.toCharArray()) {
            int cId = symbolsTable.idOf(c);
            if (cId == SymbolsTable.UNKNOWN_CHAR)
                return false;
            state = dfa.move(state, cId);
            if (state < 0)
                return false;
        }

        return dfa.isFinal(state);
    }
}
