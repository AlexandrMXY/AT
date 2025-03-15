package ru.mephi.bakinaa.regex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@RequiredArgsConstructor
public abstract class AbstractLangOperationsTest {
    @Getter private final boolean nfa;

    public static final List<RegExTestData> data = List.of(
            new RegExTestData("ab*c",
                    List.of("ac", "abc", "abbbbbc"),
                    List.of("", "aqc", "ab", "bc", "aabc")),
            new RegExTestData("a*b*c*",
                    List.of("", "aaabccc", "bbbccccc", "aaaccc", "aaabbbbbb"),
                    List.of("ba", "aqc", "bbbaaa", "cabc", "aabca")),
            new RegExTestData("a(a|b)*c+",
                    List.of("aabababbc", "ac", "abbbbbcccc"),
                    List.of("ababab", "adbbb", "bbbc", "bc")),
            new RegExTestData("qwerty",
                    List.of("qwerty"),
                    List.of("qwerty ", "qwety", "ab", "q", "")),
            new RegExTestData("a(b|$){2,4}c",
                    List.of("ac", "abbbbc", "abbc"),
                    List.of("", "abbbbbc")),
            new RegExTestData("cb+a",
                    List.of("cba", "cbbbba", "cbbbbbbbbba"),
                    List.of("", "ca", "ba", "ac", "abc")));

    public static Stream<Arguments> datasource1Arg() {
        return data.stream().map(Arguments::of);
    }

    public static Stream<Arguments> datasource2Arg() {
        return IntStream.range(0, data.size() * data.size()).mapToObj(
                (i) -> Arguments.of(data.get(i % data.size()), data.get(i / data.size())));
    }

    @ParameterizedTest
    @MethodSource("datasource1Arg")
    public void matcherTest(RegExTestData data) {
        RegEx regex = RegEx.compile(data.regex, nfa);
        for (var str : data.valid) {
            assertThat(regex.matcher(str).matches())
                    .as("Check that \"%s\" matches RE %s [NFA = %s]", str, data.regex, nfa)
                    .isTrue();
        }
        for (var str : data.invalid) {
            assertThat(regex.matcher(str).matches())
                    .as("Check that \"%s\" doesnt matches RE %s [NFA = %s]", str, data.regex, nfa)
                    .isFalse();
        }
    }

    @ParameterizedTest
    @MethodSource("datasource1Arg")
    public void inverseTest(RegExTestData data) {
        RegEx regex = RegEx.compile(data.regex, nfa);
        RegEx inverse = regex.inversion();

        for (var str : data.valid) {
            assertThat(inverse.matcher(reverse(str)).matches())
                    .as("Check that \"%s\" matches inverse of RE %s [NFA = %s]", reverse(str), data.regex, nfa)
                    .isEqualTo(regex.matcher(str).matches());
        }
        for (var str : data.invalid) {
            assertThat(inverse.matcher(reverse(str)).matches())
                    .as("Check that \"%s\" matches inverse of RE %s [NFA = %s]", reverse(str), data.regex, nfa)
                    .isEqualTo(regex.matcher(str).matches());
        }
    }

    @ParameterizedTest
    @MethodSource("datasource1Arg")
    public void restoreTest(RegExTestData data) {
        RegEx regex = RegEx.compile(data.regex, nfa);
        String restoredRE = regex.restore();
        RegEx restored = RegEx.compile(restoredRE, nfa);

        for (var str : data.valid) {
            assertThat(restored.matcher(str).matches())
                    .as("Check that \"%s\" matches restored RE for RE %s [restored = %s] [NFA = %s]", str, data.regex, restoredRE, nfa)
                    .isEqualTo(regex.matcher(str).matches());
        }
        for (var str : data.invalid) {
            assertThat(restored.matcher(str).matches())
                    .as("Check that \"%s\" matches restored RE for RE %s [restored = %s] [NFA = %s]", str, data.regex, restoredRE, nfa)
                    .isEqualTo(regex.matcher(str).matches());
        }
    }

    @ParameterizedTest
    @MethodSource("datasource2Arg")
    @EnabledIf("enableSubTest")
    public void subtractTest(RegExTestData arg1, RegExTestData arg2) {
        RegEx re1 = RegEx.compile(arg1.regex, nfa);
        RegEx re2 = RegEx.compile(arg2.regex, nfa);
        RegEx sub = re1.subtract(re2);

        Stream.concat(Stream.concat(
                arg1.valid.stream(), arg1.invalid.stream()),
                Stream.concat(arg2.valid.stream(), arg2.invalid.stream()))
                .forEach((str) -> {
                    assertThat(sub.matcher(str).matches())
                            .as("Check that \"%s\" matches sub of RE %s and RE %s [NFA = %s]", str, arg1.regex, arg2.regex, nfa)
                            .isEqualTo(
                                    re1.matcher(str).matches() && !re2.matcher(str).matches());
                });
    }



    private static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    public record RegExTestData(
            String regex,
            List<String> valid,
            List<String> invalid
    ) {}
}
