package ru.mephi.bakinaa.regex.nfa;

import lombok.*;

import java.util.*;

@ToString
public class CaptureBuffer {
    @Getter private final Map<Integer, GroupInfo> groups = new HashMap<>();
    private final Set<Integer> activeCaptures = new HashSet<>();

    // Buffer for backstep() function
    private final Stack<Integer> localBuffer = new Stack<>();

    public GroupInfo getCaptured(int groupId) {
        return groups.get(groupId);
    }

    public void beginCapterue(int groupId, int position) {
        activeCaptures.add(groupId);
        groups.put(groupId, new GroupInfo(position, position));
    }

    public void continueCapture(int groupId) {
        if (!groups.containsKey(groupId))
            throw new IllegalStateException("Unnable to continue capture");
        activeCaptures.add(groupId);
    }

    public void endCapture(int groupId) {
        activeCaptures.remove(groupId);
    }

    public void nextChar() {
        for (int grId : activeCaptures) {
            groups.get(grId).to++;
        }
    }

    public void nextChars(int cnt) {
        for (int grId : activeCaptures) {
            groups.get(grId).to += cnt;
        }
    }

    public void backstep() {
        backstep(1);
    }

    public void backstep(int cnt) {
        for (int grId : activeCaptures) {
            var gr = groups.get(grId);
            gr.to -= cnt;
            if (gr.from > gr.to) {
                groups.remove(grId);
                localBuffer.push(grId);
            }
        }

        while (!localBuffer.empty())
            activeCaptures.remove(localBuffer.pop());
    }

    @AllArgsConstructor
    @ToString
    public static class GroupInfo {
        // Inclusive
        public int from;
        // Exclusive
        public int to;

        public int len() {
            return to - from;
        }
    }
}
