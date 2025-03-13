package ru.mephi.bakinaa.regex;

public interface RegEx {
    RegExMatcher matcher(String string);

    static RegEx compile(String regex) {
        return compile(regex, false);
    }

    static RegEx compile(String regex, boolean forceNfa) {
        return new CommonRegExCompiler(regex, forceNfa).compile();
    }


    String restore();

    default RegEx inversion() {
        String restored = restore();
        System.out.println(restored);
        return new CommonRegExCompiler(restored, false, true).compile();
    }

    RegEx subtract(RegEx other);
}
