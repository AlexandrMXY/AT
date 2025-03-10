package ru.mephi.bakinaa.regex.parser;

import lombok.Getter;

public record Token(
        Type type,
        String data
) {
    private static final Token groupOpen = new Token(Type.GROPU_OPEN, null);
    private static final Token groupClose = new Token(Type.GROUP_CLOSE, null);
    private static final Token star = new Token(Type.STAR, null);
    private static final Token plus = new Token(Type.PLUS, null);
    private static final Token or = new Token(Type.OR, null);
    private static final Token end = new Token(Type.END, null);
    private static final Token concat = new Token(Type.CONCAT, null);
    private static final Token prong = new Token(Type.PRONGN, null);
    private static final Token eps = new Token(Type.EPS_CHAR, null);

    public static Token captureOpen(String id) {
        return new Token(Type.CAPTURE_OPEN, id);
    }

    public static Token groupOpen() {
        return groupOpen;
    }

    public static Token groupClose() {
        return groupClose;
    }

    public static Token star() {
        return star;
    }

    public static Token plus() {
        return plus;
    }

    public static Token or() {
        return or;
    }

    public static Token progn() {
        return prong;
    }

    public static Token end() {
        return end;
    }

    public static Token concat() {
        return concat;
    }

    public static Token eps() {
        return eps;
    }

    public static Token character(String c) {
        return new Token(Type.CHAR, c);
    }
    public static Token character(char c) {
        return new Token(Type.CHAR, String.valueOf(c));
    }
    public static Token charGroup(String s) {
        return new Token(Type.CHAR_GROUP, s);
    }

    public static Token backreference(String s) {
        return new Token(Type.BACKREFERENCE, s);
    }

    public static Token repeat(String arg) {
        return new Token(Type.REPEAT, arg);
    }

    public enum Type {
        CAPTURE_OPEN(4, false),
        GROPU_OPEN(4, false),
        GROUP_CLOSE(4, false),
        STAR(3, true),
        PLUS(3, true),
        REPEAT(3, true),
        CONCAT(2, true),
        OR(1, true),
        PRONGN(1, true),
        END(0, false),
        CHAR(0, false),
        CHAR_GROUP(0, false),
        EPS_CHAR(0, false),
        BACKREFERENCE(0, false);

        public final int priority;
        public final boolean isOperation;

        Type(int priority, boolean isOperation) {
            this.priority = priority;
            this.isOperation = isOperation;
        }
    }
}
