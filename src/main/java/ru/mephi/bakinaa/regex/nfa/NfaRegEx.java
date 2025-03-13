package ru.mephi.bakinaa.regex.nfa;

import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.regex.RegEx;
import ru.mephi.bakinaa.regex.RegExMatcher;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;

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
        throw new UnsupportedOperationException();
    }

    @Override
    public RegEx subtract(RegEx other) {
        throw new UnsupportedOperationException();
    }
}
