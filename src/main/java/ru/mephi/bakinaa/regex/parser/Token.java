package ru.mephi.bakinaa.regex.parser;

public record Token(
        Type type,
        String data
) {
    private static final Token groupOpen = new Token(Type.GROPU_OPEN, null);
    private static final Token groupClose = new Token(Type.GROUP_CLOSE, null);
    private static final Token star = new Token(Type.STAR, null);
    private static final Token or = new Token(Type.OR, null);
    private static final Token end = new Token(Type.END, null);
    private static final Token concat = new Token(Type.CONCAT, null);

    public static Token groupOpen() {
        return groupOpen;
    }

    public static Token groupClose() {
        return groupClose;
    }

    public static Token star() {
        return star;
    }

    public static Token or() {
        return or;
    }

    public static Token end() {
        return end;
    }

    public static Token concat() {
        return concat;
    }


    public static Token character(String c) {
        return new Token(Type.CHAR, c);
    }
    public static Token charGroup(String s) {
        return new Token(Type.CHAR_GROUP, s);
    }

    public enum Type {
        GROPU_OPEN,
        GROUP_CLOSE,
        STAR,
        CONCAT,
        OR,
        END,
        CHAR,
        CHAR_GROUP
    }
}
