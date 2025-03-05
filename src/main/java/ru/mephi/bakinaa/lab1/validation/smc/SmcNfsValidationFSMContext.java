package ru.mephi.bakinaa.lab1.validation.smc;

public class SmcNfsValidationFSMContext {
    public static final String HEADER = "nfs://";
    public static final int LEN_LIMIT = 63;

    public String serverName = "";
    int namesCnt = 0;
    int len = 0;
    int index = 0;

    public void incIndex() {
        index++;
    }
    public void incNamesCnt() {
        namesCnt++;
    }
    public void incLen() {
        len++;
    }
    public void pushCharToName(char c) {
        serverName += c;
    }

    public boolean checkLen() {
        return  (len >= LEN_LIMIT);
    }
    public boolean isNameEmpty() {
        return serverName.isEmpty();
    }

    public static boolean isSeparator(char c) {
        return c == '/';
    }
    public static boolean isLetter(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
    }
}
