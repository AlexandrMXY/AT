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
        RegEx regex = RegEx.compile("a*b*(aa*|b)ab*b(a|b|d)*|e|fa*b*(aa*|b)ab*b(a|b|d)*|e|fa*b*(aa*|b)ab*b(a|b|d)*|e|f");
        System.out.println(regex.matcher("aaaabbbba").matches());
        System.out.println(regex.matcher("abababab").matches());

    }
}
