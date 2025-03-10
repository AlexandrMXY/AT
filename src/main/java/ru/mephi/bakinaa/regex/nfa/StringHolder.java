package ru.mephi.bakinaa.regex.nfa;

import lombok.Getter;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;

import java.util.BitSet;
import java.util.Map;
import java.util.Stack;

public class StringHolder {
    private final SymbolsTable symbolsTable;
    private final char[] chars;

    @Getter private int index = 0;

    public StringHolder(SymbolsTable symbolsTable, String string) {
        this.symbolsTable = symbolsTable;
        this.chars = string.toCharArray();
    }

    public String substring(CaptureBuffer.GroupInfo groupInfo) {
        return new String(chars, groupInfo.from, groupInfo.to - groupInfo.from);
    }

    public boolean tryConsume(CaptureBuffer.GroupInfo groupInfo) {
        int len = groupInfo.to - groupInfo.from;
        for (int offset = 0; offset < len; offset++) {
            if(index + offset >= chars.length || chars[index + offset] != chars[groupInfo.from + offset])
                return false;
        }
        index += len;
        return true;
    }

    public void backstep(CaptureBuffer.GroupInfo groupInfo) {
        int len = groupInfo.to - groupInfo.from;
        index = Math.max(0, index - len);
    }

    public boolean popIf(int expected) {
        if (peek() == expected) {
            next();
            return true;
        }
        return false;
    }

    public int pop() {
        if (index >= chars.length)
            return symbolsTable.eol();
        return symbolsTable.idOf(chars[index++]);
    }

    public int peek() {
        if (index >= chars.length)
            return symbolsTable.eol();
        return symbolsTable.idOf(chars[index]);
    }

    public void next() {
        index++;
    }


    public void backstep() {
        if (index > 0)
            index--;
    }

}
