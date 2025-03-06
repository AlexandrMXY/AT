package ru.mephi.bakinaa.regex;

import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;

@RequiredArgsConstructor
public class RegEx {
    private final SymbolsTable symbolsTable;
    private final DFAState initialState;

    public static RegEx compile(String regex) {
        return new RegExCompiler(regex).compile();
    }

    public RegExMatcher matcher(String string) {
        return new RegExMatcher(initialState, symbolsTable, string);
    }
}
