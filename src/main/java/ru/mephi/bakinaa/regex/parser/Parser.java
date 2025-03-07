package ru.mephi.bakinaa.regex.parser;

import ru.mephi.bakinaa.regex.chars.CharGroup;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.tree.*;
import ru.mephi.bakinaa.regex.tree.raw.CharGroupNode;
import ru.mephi.bakinaa.regex.tree.raw.RawChar;

import java.util.Stack;

public class Parser {
    private final char[] string;
    private int index = 0;
    private final SymbolsTable symbolsTable;

    public static final char ESCAPE_CHAR = '%';

    public Parser(String string, SymbolsTable symbolsTable) {
        this.string = string.toCharArray();
        this.symbolsTable = symbolsTable;
    }



    public TreeNode buildTree() {
        return consumeGroup().node();
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
                // symbolsTable.registerGroup(new CharGroup(token.data().charAt(0)));
                pushNode(stack, priorities, new CharGroupNode(token.data()));
                continue;
            }
            if (token.type() == Token.Type.GROPU_OPEN) {
                pushNode(stack, priorities, consumeGroup().node);
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
        return token.type() == Token.Type.STAR;
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

            case '(' ->
                Token.groupOpen();
            case ')' ->
                Token.groupClose();
            case '*' ->
                Token.star();
            case '|' ->
                Token.or();
            case '.' ->
                Token.concat();
            case '[' ->
                Token.charGroup(readCharGroup());

            default ->
                    Token.character(c);
        };
    }

    private String readCharGroup() {
        StringBuilder builder = new StringBuilder();

        char c;
        while ((c = string[index++]) != ']') {
            if (c == ESCAPE_CHAR)
                builder.append(string[index++]);
            else
                builder.append(c);
        }

        return builder.toString();
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
