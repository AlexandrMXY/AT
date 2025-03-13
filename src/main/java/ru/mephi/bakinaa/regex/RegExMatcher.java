package ru.mephi.bakinaa.regex;

import ru.mephi.bakinaa.regex.nfa.CaptureGroups;

public interface RegExMatcher {
    boolean matches();

    CaptureGroups getCaptures();
}
