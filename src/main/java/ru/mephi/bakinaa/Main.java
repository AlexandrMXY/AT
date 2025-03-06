//package ru.mephi.bakinaa;
//
//import ru.mephi.bakinaa.regex.RegExMatcher;
//import ru.mephi.bakinaa.regex.chars.SymbolsTable;
//import ru.mephi.bakinaa.regex.chars.CharGroup;
//import ru.mephi.bakinaa.regex.tree.*;
//import ru.mephi.bakinaa.regex.parser.*;
//
//import java.io.StringReader;
//import java.util.Map;
//
//public class Main {
//    public static void main(String[] args) {
//        // a*(b|c)*a* #
//        // 1  2 3  4  5
//
//        Lexer lexer = new Lexer(new StringReader("a*b*(aa*+b)"));
//        var parser = new Parser(true);
//        parser.lexer = lexer;
//
//        parser.run();
//
//        TreeNode parsed = (TreeNode) parser.yyval().obj;
//        parsed = new Concat(parsed, new Char(parser.grIndex++));
//        GVUtils.saveTree(parsed, "parsed.png");
//
//
//
//        // a*b*(aa*+b) #
//        // 1 2  34  5  6
//
//        TreeNode root =
//                new Concat(
//                        new Concat(
//                                new Concat(
//                                        new Star(new Char(1)),
//                                        new Star(new Char(2))
//                                ),
//                                new Or(
//                                        new Concat(
//                                                new Char(3),
//                                                new Star(new Char(4))
//                                        ),
//                                        new Char(5)
//                                )
//                        ),
//                        new Char(6)
//                );
//
//        SymbolsTable st = new SymbolsTable(
//                new CharGroup('a'), // 1
//                new CharGroup('b')  // 2
//        );
//
//        // a*b*(aa*+b) #
//        // 1 2  34  5  6
//
//        Map<Integer, Integer> indexCharTable = Map.of(
//                1, 1,
//                2, 2,
//                3, 1,
//                4, 1,
//                5, 2,
//                6, SymbolsTable.EOL
//        );
//
//        GVUtils.saveTree(root, "tree.png");
//
//        RegexDFA dfa = RegexDFA.compile(
//                root,
//                st,
//                indexCharTable,
//                6
//        );
//
//        GVUtils.saveDFA(dfa, "dfa.png");
//
//        RegExMatcher matcher = new RegExMatcher(dfa.getInitialState(), st);
//        System.out.println(matcher.matches("aabbccbbaa"));
//        System.out.println(matcher.matches("aabbcacbbaa"));
//    }
//}
//


package ru.mephi.bakinaa;

import ru.mephi.bakinaa.regex.RegEx;
import ru.mephi.bakinaa.regex.RegExMatcher;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.chars.CharGroup;
import ru.mephi.bakinaa.regex.tree.*;
import ru.mephi.bakinaa.regex.parser.*;

import java.io.StringReader;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        RegEx regex = RegEx.compile("a*.b*.(a.a*|b)");

        System.out.println(regex.matcher("aaaabbbba").matches());
        System.out.println(regex.matcher("abababab").matches());
    }
}