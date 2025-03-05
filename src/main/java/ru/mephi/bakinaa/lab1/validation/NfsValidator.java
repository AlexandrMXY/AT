package ru.mephi.bakinaa.lab1.validation;

public interface NfsValidator {
    int MAX_LEN = 63;
    int MAX_LENGTH_INCLUDE_HEADER = MAX_LEN + "nfs://".length();

    String getServerName(String string);

    default boolean isValid(String string) {
        return getServerName(string) != null;
    }
}
