package ru.mephi.bakinaa.lab1.validation.flex;

public record FlexNfsToken(
        TokenType type,
        String content
) {
    public static FlexNfsToken header() {
        return new FlexNfsToken(TokenType.HEADER, null);
    }

    public static FlexNfsToken dir(String name) {
        return new FlexNfsToken(TokenType.DIR, name);
    }

    public enum TokenType {
        HEADER,
        DIR
    }
}
