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
        RegEx regex = RegEx.compile("a*b*(aa*|b)ab*b(a|b|d)*|e|f");
        System.out.println(regex.matcher("aaaabbbba").matches());
        System.out.println(regex.matcher("abababab").matches());

//        GVUtils.saveTree(new Parser("a*b*(aa*|b)").buildTree(), "pr.png");


        SymbolsTable st = new SymbolsTable();
        st.registerGroup(new CharGroup('a'));
        st.registerGroup(new CharGroup('b'));

        int index = 1;
        DFAState A = new DFAState(index++); //
        DFAState B = new DFAState(index++);
        DFAState C = new DFAState(index++);
        DFAState D = new DFAState(index++);
        DFAState E = new DFAState(index++);
        DFAState F = new DFAState(true, index++);
        DFAState G = new DFAState(true, index++);
        DFAState H = new DFAState(index++);

        A.transitions.put(1, H);
        A.transitions.put(2, B);

        B.transitions.put(1, H);
        B.transitions.put(2, A);

        C.transitions.put(1, E);
        C.transitions.put(2, F);

        D.transitions.put(1, E);
        D.transitions.put(2, F);

        E.transitions.put(1, F);
        E.transitions.put(2, G);

        F.transitions.put(1, F);
        F.transitions.put(2, F);

        G.transitions.put(1, G);
        G.transitions.put(2, F);

        H.transitions.put(1, C);
        H.transitions.put(2, C);


    }
}