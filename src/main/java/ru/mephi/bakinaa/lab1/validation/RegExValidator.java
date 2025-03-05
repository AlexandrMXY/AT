package ru.mephi.bakinaa.lab1.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExValidator implements NfsValidator {
    public static final Pattern PATTERN =
            Pattern.compile("nfs://((?<server>[a-zA-Z]+)(/[a-zA-Z]+)+)");

    @Override
    public String getServerName(String string) {
        if (string == null || string.length() > NfsValidator.MAX_LENGTH_INCLUDE_HEADER)
            return null;
        Matcher matcher = PATTERN.matcher(string);
        if (!matcher.matches())
            return null;
        return matcher.group("server");
    }
}
