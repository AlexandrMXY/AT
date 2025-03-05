package ru.mephi.bakinaa.lab1.validation.flex;

import ru.mephi.bakinaa.lab1.validation.NfsValidator;

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;

public class FlexValidator implements NfsValidator {
    @Override
    public String getServerName(String string) {
        if (string == null || string.length() > NfsValidator.MAX_LENGTH_INCLUDE_HEADER)
            return null;

        try {
            LexNfsLexer lexer = new LexNfsLexer(new StringReader(string));
            FlexNfsToken token;

            int tokenIndex = 0;
            String server = null;

            while ((token = lexer.yylex()) != null) {
                if (tokenIndex != 0 && token.type() == FlexNfsToken.TokenType.HEADER)
                    return null;
                if (tokenIndex == 0 && token.type() == FlexNfsToken.TokenType.DIR)
                    return null;
                if (tokenIndex == 1)
                    server = token.content().substring(1);
                tokenIndex++;
            }

            return tokenIndex >= 3 ? server : null;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (FlexValidationException exception) {
            return null;
        }
    }
}
