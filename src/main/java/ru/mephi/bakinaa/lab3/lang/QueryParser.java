package ru.mephi.bakinaa.lab3.lang;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;
import ru.mephi.bakinaa.lab3.utils.GVUtils;

import java.io.StringReader;

@Slf4j
public class QueryParser {
    private final Lexer lexer;
    private final YYParser parser;

    public QueryParser(String query) {
        lexer = new Lexer(new StringReader(query));
        parser = new YYParser(true);
        parser.parser = this;
    }

    public TreeNode parse() {
        try {
            parser.yyparse();
            var res = (TreeNode) parser.yyval.obj;
            System.out.println(res);
            return res;
        } catch (LangException e) {
            GVUtils.save((TreeNode) parser.yyval.obj, "e.png");
            throw e;
        }
    }

    @SneakyThrows
    int yylex() {
        if (lexer.yyatEOF())
            return 0;
        try {
            Token t = lexer.yylex();
            if (t == null)
                return 0;
            System.out.println(t);
            parser.yylval = new YYParserVal(t.data());
            return tokId(t);
        } catch (Throwable t) {
            log.atError()
                    .setMessage("Parser error")
                    .setCause(t)
                    .log();
        }
        return 0;
    }

    private int tokId(Token token) {
        return switch (token.type()) {
            case INT_NUM -> YYParser.INT_NUM;
            case FLOAT_NUM -> YYParser.FLOAT_NUM;
            case STRING -> YYParser.STRING;
            case TRUE -> YYParser.TRUE;
            case FALSE -> YYParser.FALSE;
            case NULL -> YYParser.NULL;
            case IDENTIFIER -> YYParser.ID;

            case INDEX_TYPE -> YYParser.INDEX_TYPE;
            case TYPE_NAME -> YYParser.TYPE_NAME;
            case MODIFIER -> YYParser.MODIFIER;
            case CONSTRAINT -> YYParser.CONSTRAINT;

            case RELATIONSHIP -> YYParser.RELATIONSHIP;
            case ROW -> YYParser.ROW;

            case PAR_OPEN -> YYParser.PAR_OPEN;
            case PAR_CLOSE -> YYParser.PAR_CLOSE;
            case SQUARE_BR_OPEN -> YYParser.SQUARE_BR_OPEN;
            case SQUARE_BR_CLOSE -> YYParser.SQUARE_BR_CLOSE;
            case CUR_BR_OPEN -> YYParser.CUR_BR_OPEN;
            case CUR_BR_CLOSE -> YYParser.CUR_BR_CLOSE;

            case COMA -> YYParser.COMA;
            case DOT -> YYParser.DOT;
            case SEMICOLON -> YYParser.SEMICOLON;
            case SCOPE_OPERATOR -> YYParser.SCOPE_OPERATOR;
            case ARROW -> YYParser.ARROW;

            case EQUALS -> YYParser.EQUALS;
            case NOT_EQUALS -> YYParser.NOT_EQUALS;
            case GREATER -> YYParser.GREATER;
            case LESS -> YYParser.LESS;
            case GREATER_EQ -> YYParser.GREATER_EQ;
            case LESS_EQ -> YYParser.LESS_EQ;

            case OR -> YYParser.OR;
            case AND -> YYParser.AND;
            case NOT -> YYParser.NOT;

            case ASSIGN -> YYParser.ASSIGN;
        };
    }
}
