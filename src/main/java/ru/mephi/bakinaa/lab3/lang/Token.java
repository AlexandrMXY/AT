package ru.mephi.bakinaa.lab3.lang;

public record Token(
        TokenType type,
        String data
) {
    public Token(TokenType type) {
        this(type, null);
    }

}
