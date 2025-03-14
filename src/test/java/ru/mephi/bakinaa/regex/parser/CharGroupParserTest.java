package ru.mephi.bakinaa.regex.parser;

import org.junit.jupiter.api.Test;
import ru.mephi.bakinaa.regex.chars.CharGroup;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class CharGroupParserTest {
    @Test
    public void parse_1letter() {
        var g = new CharGroupParser("a").parse();
        assertThatList(g).containsExactlyInAnyOrderElementsOf(List.of(
                new CharGroup('a', 'a')
        ));
    }

    @Test
    public void parse_1segment() {
        var g = new CharGroupParser("a-z").parse();
        assertThatList(g).containsExactlyInAnyOrderElementsOf(List.of(
                new CharGroup('a', 'z')
        ));
    }

    @Test
    public void parse_2letter() {
        var g = new CharGroupParser("ab").parse();
        assertThatList(g).containsExactlyInAnyOrderElementsOf(List.of(
                new CharGroup('a', 'a'),
                new CharGroup('b', 'b')
        ));
    }

    @Test
    public void parse_1letter1segment() {
        var g = new CharGroupParser("ac-z").parse();
        assertThatList(g).containsExactlyInAnyOrderElementsOf(List.of(
                new CharGroup('a', 'a'),
                new CharGroup('c', 'z')
        ));
    }

    @Test
    public void parse_2segments() {
        var g = new CharGroupParser("a-zb-c").parse();
        assertThatList(g).containsExactlyInAnyOrderElementsOf(List.of(
                new CharGroup('a', 'z'),
                new CharGroup('b', 'c')
        ));
    }

    @Test
    public void parse_multipleElements() {
        var g = new CharGroupParser("a-zb-cfgh").parse();
        assertThatList(g).containsExactlyInAnyOrderElementsOf(List.of(
                new CharGroup('a', 'z'),
                new CharGroup('b', 'c'),
                new CharGroup('f', 'f'),
                new CharGroup('g', 'g'),
                new CharGroup('h', 'h')
        ));
    }

    @Test
    public void parse_escapeChars() {
        var g = new CharGroupParser("%-%%%]").parse();
        assertThatList(g).containsExactlyInAnyOrderElementsOf(List.of(
                new CharGroup('%', '%'),
                new CharGroup('-', '-'),
                new CharGroup(']', ']')
        ));
    }

    @Test
    public void parse_noOneOfSegmentBorders_shouldThrow() {
        assertThrows(ParserException.class, () ->
                new CharGroupParser("a-").parse());

        assertThrows(ParserException.class, () ->
                new CharGroupParser("-a").parse());
    }

    @Test
    public void parse_doubleMinus_shouldThrow() {
        assertThrows(ParserException.class, () ->
                new CharGroupParser("a--b").parse());

        assertThrows(ParserException.class, () ->
                new CharGroupParser("a--").parse());
    }

    @Test
    public void parse_invalidEscapeChar_shouldThrow() {
        assertThrows(ParserException.class, () ->
                new CharGroupParser("%").parse());
    }
}