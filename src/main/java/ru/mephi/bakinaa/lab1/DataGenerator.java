package ru.mephi.bakinaa.lab1;

import ru.mephi.bakinaa.lab1.validation.NfsValidator;

import java.util.Random;

public class DataGenerator {
    public static final String LETTERS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    public static final String INVALID_CHARS = "1234567890";
    public static final String SEPARATOR = "/";
    public static final String HEADER0 = "nfs:/";
    public static final int MAX_LEN = NfsValidator.MAX_LENGTH_INCLUDE_HEADER;

    private static final int MAX_DIR_NAME_LEN = 70;

    private final Random random = new Random();

    public String next(int len) {
        return next0(len).toString();
    }

    private StringBuilder next0(int len) {
        if (len > MAX_LEN || len < 9)
            throw new IllegalArgumentException("Unable to create valid string with len " + len + " Len should be between 9 and " + MAX_LEN);

        StringBuilder builder = new StringBuilder();
        builder.append(HEADER0);

        while (builder.length() < len) {
            if (len - builder.length() <= 3) {
                appendSubDir(builder, 2);
                break;
            }
            int segLen = Math.min(
                    MAX_DIR_NAME_LEN,
                    random.nextInt(2, len - builder.length()));
            appendSubDir(builder, segLen);
        }

        return builder;
    }

    public String nextInvalid(int len) {
        if (len <= 9)
            return appendChars(new StringBuilder(), len, LETTERS).toString();
        if (len > MAX_LEN)
            return appendChars(next0(MAX_LEN), len - MAX_LEN, INVALID_CHARS).toString();

        StringBuilder builder = next0(len - 1);
        builder.insert(
                random.nextInt(0, builder.length()),
                INVALID_CHARS.charAt(random.nextInt(INVALID_CHARS.length())));
        return builder.toString();
    }

    private void appendSubDir(StringBuilder builder, int len) {
        builder.append(SEPARATOR);
        random
                .ints(len - 1, 0, DataGenerator.LETTERS.length())
                .forEach((i) -> builder.append(DataGenerator.LETTERS.charAt(i)));
    }

    private StringBuilder appendChars(StringBuilder builder, int cnt, String letters) {
        random
                .ints(cnt, 0, letters.length())
                .forEach((i) -> builder.append(letters.charAt(i)));
        return builder;
    }
}
