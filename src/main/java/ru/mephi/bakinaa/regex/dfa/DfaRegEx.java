package ru.mephi.bakinaa.regex.dfa;

import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.regex.RegEx;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.dfa.DFAState;
import ru.mephi.bakinaa.regex.dfa.DfaRegExMatcher;

@RequiredArgsConstructor
public class DfaRegEx implements RegEx {
    private final SymbolsTable symbolsTable;
    private final DFAState initialState;

    public DfaRegExMatcher matcher(String string) {
        return new DfaRegExMatcher(initialState, symbolsTable, string);
    }
}
