package ru.mephi.bakinaa.lab3.lang;

import ru.mephi.bakinaa.lab3.lang.enums.TokenType;

public record Token(
        TokenType type,
        String data
) {
    public Token(TokenType type) {
        this(type, null);
    }

}
