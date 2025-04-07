package ru.mephi.bakinaa.lab3.lang.enums;

import ru.mephi.bakinaa.lab3.lang.Token;
import ru.mephi.bakinaa.lab3.lang.YYParser;

public enum TokenType {
    INT_NUM(YYParser.INT_NUM),
    FLOAT_NUM(YYParser.FLOAT_NUM),
    STRING(YYParser.STRING),
    TRUE(YYParser.TRUE),
    FALSE(YYParser.FALSE),
    NULL(YYParser.NULL),
    IDENTIFIER(YYParser.ID),

    INDEX_TYPE(YYParser.INDEX_TYPE),
    TYPE_NAME(YYParser.TYPE_NAME),
    MODIFIER(YYParser.MODIFIER),
    CONSTRAINT(YYParser.CONSTRAINT),

    RELATIONSHIP(YYParser.RELATIONSHIP),
    ROW(YYParser.ROW),

    PAR_OPEN(YYParser.PAR_OPEN),
    PAR_CLOSE(YYParser.PAR_CLOSE),
    SQUARE_BR_OPEN(YYParser.SQUARE_BR_OPEN),
    SQUARE_BR_CLOSE(YYParser.SQUARE_BR_CLOSE),
    CUR_BR_OPEN(YYParser.CUR_BR_OPEN),
    CUR_BR_CLOSE(YYParser.CUR_BR_CLOSE),

    COMA(YYParser.COMA),
    DOT(YYParser.DOT),
    SEMICOLON(YYParser.SEMICOLON),
    SCOPE_OPERATOR(YYParser.SCOPE_OPERATOR),
    ARROW(YYParser.ARROW),

    EQUALS(YYParser.EQUALS),
    NOT_EQUALS(YYParser.NOT_EQUALS),
    GREATER(YYParser.GREATER),
    LESS(YYParser.LESS),
    GREATER_EQ(YYParser.GREATER_EQ),
    LESS_EQ(YYParser.LESS_EQ),

    OR(YYParser.OR),
    AND(YYParser.AND),
    NOT(YYParser.NOT),

    ADD(YYParser.ADD),
    SUB(YYParser.SUB),
    MUL(YYParser.MUL),
    DIV(YYParser.DIV),

    ASC(YYParser.ASC),
    DESC(YYParser.DESC),

    ASSIGN(YYParser.ASSIGN);

    private final Token instance = new Token(this);
    
    public final short id;

    TokenType(short id) {
        this.id = id;
    }

    public Token instance() {
        return instance;
    }

    public Token instance(String data) {
        return new Token(this, data);
    }
}
