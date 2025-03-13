package ru.mephi.bakinaa.regex.nfa;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class CaptureGroups {
    private List<String> groups;

    CaptureGroups(Map<Integer, String> captures) {
        if (captures == null)
            return;
        groups = new ArrayList<>(captures.size());
        captures.forEach((id, str) -> {
            while (groups.size() <= id)
                groups.add(null);
            groups.set(id, str);
        });
    }

    public String get(int index) {
        if (groups == null || index < 0 || index >= groups.size())
            throw new IllegalStateException("Invalid group access");
        String s = groups.get(index);
        if (s == null)
            throw new IllegalStateException("Invalid group access");
        return s;
    }
}
