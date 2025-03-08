package ru.mephi.bakinaa.regex.parser;

import ru.mephi.bakinaa.regex.chars.CharGroup;

import java.util.ArrayList;
import java.util.List;

public class CharGroupParser {
    public static final char ESCAPE_CHAR = Parser.ESCAPE_CHAR;
    private final char[] chars;
    private int index = 0;

    public CharGroupParser(String s) {
        this.chars = s.toCharArray();
    }

    public List<CharGroup> parse() {
        List<CharGroup> res = new ArrayList<>();

        try {
            CharGroup next;
            while ((next = consumeGroup()) != null)
                res.add(next);
        } catch (IndexOutOfBoundsException e) {
            throw new ParserException("Syntax error");
        }

        return res;
    }

    private CharGroup consumeGroup() {
        char a;
        char b;

        if (index == chars.length)
            return null;

        if ((a = chars[index++]) == ESCAPE_CHAR)
            a = chars[index++];
        else {
            if (a == '-')
                throw new ParserException("Syntax error");
        }

        if (index < chars.length && chars[index] == '-') {
            index++;
            if ((b = chars[index++]) == ESCAPE_CHAR) {
                b = chars[index++];
            }
            else {
                if (b == '-')
                    throw new ParserException("Syntax error");
            }


            return new CharGroup(a, b);
        }

        return new CharGroup(a);
    }
}
