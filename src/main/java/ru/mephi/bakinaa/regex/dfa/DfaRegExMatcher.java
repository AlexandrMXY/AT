package ru.mephi.bakinaa.regex.dfa;

import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.regex.RegExMatcher;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;

@RequiredArgsConstructor
public class DfaRegExMatcher implements RegExMatcher {
    private final DFAState initState;
    private final SymbolsTable symbolsTable;
    private final String string;

    @Override
    public boolean matches() {
        DFAState state = initState;

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
