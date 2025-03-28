package ru.mephi.bakinaa.lab3.lang;

public enum TokenType {
    INT_NUM,
    FLOAT_NUM,
    STRING,
    TRUE,
    FALSE,
    NULL,
    IDENTIFIER,

    PAR_OPEN,
    PAR_CLOSE,
    SQUARE_BR_OPEN,
    SQUARE_BR_CLOSE,
    CUR_BR_OPEN,
    CUR_BR_CLOSE,

    COMA,
    DOT,
    SEMICOLON,
    SCOPE_OPERATOR,

    EQUALS,
    NOT_EQUALS,
    GREATER,
    LESS,
    GREATER_EQ,
    LESS_EQ,

    OR,
    AND,
    NOT,

    ASSIGN;

    private final Token instance = new Token(this);

    public Token instance() {
        return instance;
    }

    public Token instance(String data) {
        return new Token(this, data);
    }
}
