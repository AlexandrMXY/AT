package ru.mephi.bakinaa.lab3.lang;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.bakinaa.lab3.commons.Expressions;
import ru.mephi.bakinaa.lab3.db.registry.Registry;
import ru.mephi.bakinaa.lab3.exceptions.LangException;
import ru.mephi.bakinaa.lab3.utils.GVUtils;
import ru.mephi.bakinaa.lab3.utils.ParserUtils;

import java.io.StringReader;

@Slf4j
public class QueryParser {
    private final Lexer lexer;
    private final YYParser parser;

    public QueryParser(String query, Registry registry) {
        lexer = new Lexer(new StringReader(query));
        parser = new YYParser(false);
        parser.parser = this;
        parser.util = new ParserUtils(registry);
    }

    public Expressions parse() {
        parser.yyparse();
        var res = (Expressions) parser.yyval.obj;
        //System.out.println(res);
        return res;
    }

    @SneakyThrows
    int yylex() {
        if (lexer.yyatEOF())
            return 0;
        try {
            Token t = lexer.yylex();
            if (t == null)
                return 0;
            //System.out.println(t);
            parser.yylval = new YYParserVal(t.data());
            return t.type().id;
        } catch (Throwable t) {
            log.atError()
                    .setMessage("Parser error")
                    .setCause(t)
                    .log();
        }
        return 0;
    }
}
