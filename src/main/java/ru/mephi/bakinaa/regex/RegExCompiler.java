package ru.mephi.bakinaa.regex;

import ru.mephi.bakinaa.regex.parser.Lexer;
import ru.mephi.bakinaa.regex.parser.Parser;
import ru.mephi.bakinaa.regex.tree.Char;
import ru.mephi.bakinaa.regex.tree.Concat;
import ru.mephi.bakinaa.regex.tree.TreeNode;

import java.io.StringReader;

public class RegExCompiler {
    private final String regexString;
    private TreeNode root;

    public RegExCompiler(String regexString) {
        this.regexString = regexString;
    }

    public void compile() {

    }

    private void buildTree() {
        Lexer lexer = new Lexer(new StringReader("a*b*(aa*+b)"));
        Parser parser = new Parser(true);
        parser.lexer = lexer;

        parser.run();

        TreeNode parsed = (TreeNode) parser.yyval().obj;
        root = new Concat(parsed, new Char(parser.grIndex++));
    }

    private void rebuildTree() {

    }
}
