package ru.mephi.bakinaa.regex.parser;

import ru.mephi.bakinaa.regex.tree.TreeNode;
import ru.mephi.bakinaa.regex.tree.raw.Repeat;

public class RepeatParser {
    /**
     * Парсит строку без пробелов в Repeat узел
     * @param node дочерний узел
     * @param str строка
     * @return полученный Repeat узел
     */
    public static Repeat createRepeat(TreeNode node, String str) {
        int separatorIndex = str.indexOf(',');
        if (separatorIndex == -1)
            throw new ParserException("Syntax error");

        int a = 0;
        int b = Integer.MAX_VALUE;

        try {
            if (separatorIndex != 0) {
                a = Integer.parseInt(str, 0, separatorIndex, 10);
            }
            if (separatorIndex != str.length() - 1) {
                b = Integer.parseInt(str, separatorIndex + 1, str.length(), 10);
            }
        } catch (NumberFormatException e) {
            throw new ParserException(e);
        }

        if (a < 0 || b < 0 || a > b)
            throw new ParserException("Syntax error");

        return new Repeat(node, a, b);
    }

}
