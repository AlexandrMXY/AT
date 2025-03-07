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
    public static Token character(char c) {
        return new Token(Type.CHAR, String.valueOf(c));
    }
    public static Token charGroup(String s) {
        return new Token(Type.CHAR_GROUP, s);
    }

    public enum Type {
        GROPU_OPEN(4, false),
        GROUP_CLOSE(4, false),
        STAR(3, true),
        CONCAT(2, true),
        OR(1, true),
        END(0, false),
        CHAR(0, false),
        CHAR_GROUP(0, false);

        public final int priority;
        public final boolean isOperation;

        Type(int priority, boolean isOperation) {
            this.priority = priority;
            this.isOperation = isOperation;
        }
    }
}
