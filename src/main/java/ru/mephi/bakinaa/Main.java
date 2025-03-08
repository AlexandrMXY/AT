package ru.mephi.bakinaa;

import ru.mephi.bakinaa.regex.DFAState;
import ru.mephi.bakinaa.regex.RegEx;
import ru.mephi.bakinaa.regex.RegExMatcher;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.chars.CharGroup;
import ru.mephi.bakinaa.regex.tree.*;
import ru.mephi.bakinaa.regex.parser.*;

import java.io.StringReader;
import java.util.BitSet;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        RegEx regex = RegEx.compile("a*b*(aa*|b)");
        RegEx regex = RegEx.compile("abc/def#(d{1,1}(ef[qw]+))");

        System.out.println(regex.matcher("abc").matches());
        System.out.println(regex.matcher("ac").matches());

    }
}
