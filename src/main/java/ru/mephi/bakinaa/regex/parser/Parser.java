package ru.mephi.bakinaa.regex.parser;

import ru.mephi.bakinaa.regex.chars.CharGroup;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.tree.*;
import ru.mephi.bakinaa.regex.tree.raw.CharGroupNode;
import ru.mephi.bakinaa.regex.tree.raw.Plus;
import ru.mephi.bakinaa.regex.tree.raw.Progn;
import ru.mephi.bakinaa.regex.tree.raw.RawChar;

import java.util.List;
import java.util.Stack;

public class Parser {
    private final char[] string;
    private int index = 0;
    private final SymbolsTable symbolsTable;
    private int nextCaptureId = 0;

    public static final char ESCAPE_CHAR = '%';

    public Parser(String string, SymbolsTable symbolsTable) {
        this.string = string.toCharArray();
        this.symbolsTable = symbolsTable;
    }



    public TreeNode buildTree() {
        var res = consumeGroup().node();
        if (index < string.length)
            throw new ParserException("Syntax error");
        return res;
    }


    private Expr consumeGroup() {
        Stack<Expr> stack = new Stack<>();
        Stack<Integer> priorities = new Stack<>();

        Token token;
        while ((token = nextToken()) != null) {
            if (token.type() == Token.Type.CHAR) {
                symbolsTable.registerGroup(new CharGroup(token.data().charAt(0)));
                pushNode(stack, priorities, new RawChar(token.data()));
                continue;
            }
            if (token.type() == Token.Type.CHAR_GROUP) {
                // TODO
                List<CharGroup> groups = new CharGroupParser(token.data()).parse();
                symbolsTable.registerGroups(groups);
                pushNode(stack, priorities, new CharGroupNode(groups));
                continue;
            }
            if (token.type() == Token.Type.EPS_CHAR) {
                pushNode(stack, priorities, new EpsChar());
                continue;
            }
            if (token.type() == Token.Type.GROPU_OPEN) {
                pushNode(stack, priorities, consumeGroup().node);
                continue;
            }
            if (token.type() == Token.Type.CAPTURE_OPEN) {
                pushNode(stack, priorities, new Capture(consumeGroup().node, Integer.parseInt(token.data())));
                continue;
            }
            if (token.type() == Token.Type.BACKREFERENCE) {
                pushNode(stack, priorities, new Backreference(Integer.parseInt(token.data())));
                continue;
            }

            if (token.type() == Token.Type.GROUP_CLOSE ||
                token.type() == Token.Type.END) {

                while (stack.size() > 1) {
                    stack.push(new Expr(consumeOperation(stack)));
                    priorities.pop();
                }

                return stack.pop();
            }

            pushOperation(stack, priorities, token);
        }
        throw new ParserException("Syntax error");
    }


    private void pushNode(Stack<Expr> stack, Stack<Integer> priorities, TreeNode node) {
        if (!stack.empty() && (stack.peek().isNode() || isUnary(stack.peek().token))) {
            pushOperation(stack, priorities, Token.concat());
        }
        stack.push(new Expr(node));
    }

    private void pushOperation(Stack<Expr> stack, Stack<Integer> priorities, Token operation) {
        int opPriority = operation.type().priority;

        while (!priorities.empty() && priorities.peek() >= opPriority) {
            stack.push(new Expr(consumeOperation(stack)));
            priorities.pop();
        }

        // Унарные операции над отделным элементом
        if (priorities.empty() && isUnary(operation)) {
            stack.push(new Expr(operation));
            stack.push(new Expr(consumeOperation(stack)));
        } else {
            priorities.push(opPriority);
            stack.push(new Expr(operation));
        }
    }

    public boolean isUnary(Token token) {
        if (token == null)
            return false;
        return  token.type() == Token.Type.STAR ||
                token.type() == Token.Type.PLUS ||
                token.type() == Token.Type.REPEAT;
    }

    private TreeNode consumeOperation(Stack<Expr> stack) {
        checkStack(stack);
        Expr op1 = stack.pop();

        if (op1.isToken()) {
            switch (op1.token().type()) {
                case STAR -> {
                    Expr operand = stack.pop();
                    if (!operand.isNode())
                        throw new ParserException("Syntax error");
                    return new Star(operand.node());
                }
                case PLUS -> {
                    Expr operand = stack.pop();
                    if (!operand.isNode())
                        throw new ParserException("Syntax error");
                    return new Plus(operand.node());
                }
                case REPEAT -> {
                    Expr operand = stack.pop();
                    if (!operand.isNode())
                        throw new ParserException("Syntax error");
                    return RepeatParser.createRepeat(operand.node(), op1.token().data());
                }

                default ->
                    throw new ParserException("Syntax error");
            }
        }

        checkStack(stack);
        Expr op2 = stack.pop();
        if (!op2.isToken())
            throw new ParserException("Syntax error");

        checkStack(stack);
        Expr op3 = stack.pop();
        if (!op3.isNode())
            throw new ParserException("Syntax error");

        switch (op2.token().type()) {
            case OR -> {
                return new Or(op3.node(), op1.node());
            }
            case CONCAT -> {
                return new Concat(op3.node(), op1.node());
            }
            case PRONGN -> {
                return new Progn(op3.node(), op1.node());
            }
            default ->
                throw new ParserException("Syntax error");
        }
    }

    private void checkStack(Stack<?> stack) {
        if (stack.empty())
            throw new ParserException("Syntax error");
    }

    private Token nextToken() {
        if (index >= string.length)
            return index++ == string.length ? Token.end() : null;

        char c = string[index++];

        return switch (c) {
            case ESCAPE_CHAR ->
                Token.character(string[index++]);

            case '#' ->
                readCapturChar();
            case '(' ->
                Token.groupOpen();
            case ')' ->
                Token.groupClose();
            case '*' ->
                Token.star();
            case '+' ->
                Token.plus();
            case '|' ->
                Token.or();
            case '/' ->
                Token.progn();
            case '.' ->
                Token.concat();
            case '[' ->
                Token.charGroup(readCharGroup());
            case '$' ->
                Token.eps();
            case '{' ->
                Token.repeat(readRepeatBracket());


            default ->
                    Token.character(c);
        };
    }

    private String readCharGroup() {
        StringBuilder builder = new StringBuilder();

        char c;
        while ((c = string[index++]) != ']') {
            if (c == ESCAPE_CHAR && string[index] == ']')
                builder.append(string[index++]);
            else
                builder.append(c);
        }

        return builder.toString();
    }

    private String readRepeatBracket() {
        StringBuilder builder = new StringBuilder();

        char c;
        while ((c = string[index++]) != '}') {
            if (Character.isWhitespace(c))
                continue;
            builder.append(c);
        }

        return builder.toString();
    }

    private Token readCapturChar() {
        if (index == string.length)
            throw new ParserException("Syntax error");

        if (string[index] == '(') {
            index++;
            return Token.captureOpen(String.valueOf(nextCaptureId++));
        }
        if (isDigit(string[index])) {
            return Token.backreference(readNumber());
        }
        throw new ParserException("Syntax error");
    }

    private String readNumber() {
        StringBuilder builder = new StringBuilder();

        while (index < string.length && isDigit(string[index]))
            builder.append(string[index++]);

        return builder.toString();
    }

    private boolean isDigit(char c) {
        return '0' <= c && '9' >= c;
    }

    private record Expr(
            Token token,
            TreeNode node
    ) {
        private Expr(Token token, TreeNode node) {
            this.token = token;
            this.node = node;
        }

        Expr(Token token) {
            this(token, null);
        }

        Expr(TreeNode node) {
            this(null, node);
        }

        boolean isNode() {
            return node != null;
        }

        boolean isToken() {
            return token != null;
        }
    }
}
