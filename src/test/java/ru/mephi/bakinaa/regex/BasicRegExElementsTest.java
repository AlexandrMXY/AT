package ru.mephi.bakinaa.regex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicRegExElementsTest {

    @Test
    public void operatrions_star() {
        RegEx reg = RegEx.compile("a*");

        assertTrue(reg.matcher("").matches());
        assertTrue(reg.matcher("aaaa").matches());
        assertTrue(reg.matcher("aaaaaaa").matches());
        assertFalse(reg.matcher("aaabaaa").matches());
    }

    @Test
    public void operatrions_or() {
        RegEx reg = RegEx.compile("a|b");

        assertTrue(reg.matcher("a").matches());
        assertTrue(reg.matcher("b").matches());
        assertFalse(reg.matcher("c").matches());
        assertFalse(reg.matcher("ab").matches());
        assertFalse(reg.matcher("").matches());
    }

    @Test
    public void operatrions_concat() {
        RegEx reg = RegEx.compile("ab");

        assertFalse(reg.matcher("a").matches());
        assertFalse(reg.matcher("b").matches());
        assertFalse(reg.matcher("c").matches());
        assertTrue(reg.matcher("ab").matches());
        assertFalse(reg.matcher("ba").matches());
        assertFalse(reg.matcher("").matches());
    }

    @Test
    public void operatrions_concat_dot() {
        RegEx reg = RegEx.compile("a.b");

        assertFalse(reg.matcher("a").matches());
        assertFalse(reg.matcher("b").matches());
        assertFalse(reg.matcher("c").matches());
        assertTrue(reg.matcher("ab").matches());
        assertFalse(reg.matcher("ba").matches());
        assertFalse(reg.matcher("b.a").matches());
        assertFalse(reg.matcher("").matches());
    }

    @Test
    public void operatrions_plus() {
        RegEx reg = RegEx.compile("a+");

        assertFalse(reg.matcher("").matches());
        assertTrue(reg.matcher("a").matches());
        assertTrue(reg.matcher("aaaa").matches());
        assertTrue(reg.matcher("aaaaaaa").matches());
        assertFalse(reg.matcher("aaabaaa").matches());
        assertFalse(reg.matcher("b").matches());
    }

    @Test
    public void operatrions_charGroups() {
        RegEx reg = RegEx.compile("[a-f]");

        assertTrue(reg.matcher("a").matches());
        assertTrue(reg.matcher("f").matches());
        assertTrue(reg.matcher("d").matches());
        assertFalse(reg.matcher("").matches());
        assertFalse(reg.matcher("z").matches());
        assertFalse(reg.matcher("A").matches());
        assertFalse(reg.matcher("aa").matches());
    }

    @Test
    public void operatrions_charGroups2() {
        RegEx reg = RegEx.compile("[a-f][c-h]");

        assertTrue(reg.matcher("ah").matches());
        assertTrue(reg.matcher("ee").matches());
        assertTrue(reg.matcher("ac").matches());
        assertTrue(reg.matcher("bg").matches());
        assertFalse(reg.matcher("gg").matches());
        assertFalse(reg.matcher("bb").matches());
        assertFalse(reg.matcher("ahh").matches());
    }

    @Test
    public void operatrions_charGroups3() {
        RegEx reg = RegEx.compile("[a-cx-z][%%-%-%]]*");

        assertTrue(reg.matcher("a%").matches());
        assertTrue(reg.matcher("b-").matches());
        assertTrue(reg.matcher("y%-%").matches());
        assertTrue(reg.matcher("y]]").matches());
        assertFalse(reg.matcher("h%").matches());
        assertFalse(reg.matcher("").matches());
        assertFalse(reg.matcher("%").matches());
    }

    @Test
    public void operatrions_empty() {
        RegEx reg = RegEx.compile("$");

        assertTrue(reg.matcher("").matches());
        assertFalse(reg.matcher("$").matches());
        assertFalse(reg.matcher("d").matches());
        assertFalse(reg.matcher("z").matches());
        assertFalse(reg.matcher("A").matches());
        assertFalse(reg.matcher("aa").matches());
    }

    @Test
    public void operatrions_emptyOr() {
        RegEx reg = RegEx.compile("$|a");

        assertTrue(reg.matcher("").matches());
        assertTrue(reg.matcher("a").matches());
        assertFalse(reg.matcher("$").matches());
        assertFalse(reg.matcher("d").matches());
        assertFalse(reg.matcher("z").matches());
        assertFalse(reg.matcher("A").matches());
        assertFalse(reg.matcher("aa").matches());
    }

    @Test
    public void operatrions_repeat() {
        RegEx reg = RegEx.compile("a{2,4}");

        assertFalse(reg.matcher("").matches());
        assertFalse(reg.matcher("a").matches());
        assertTrue(reg.matcher("aa").matches());
        assertTrue(reg.matcher("aaa").matches());
        assertTrue(reg.matcher("aaaa").matches());
        assertFalse(reg.matcher("aaaaa").matches());
        assertFalse(reg.matcher("bbb").matches());
    }

    @Test
    public void operatrions_repeatNoLeftArg() {
        RegEx reg = RegEx.compile("a{,4}");

        assertTrue(reg.matcher("").matches());
        assertTrue(reg.matcher("a").matches());
        assertTrue(reg.matcher("aa").matches());
        assertTrue(reg.matcher("aaa").matches());
        assertTrue(reg.matcher("aaaa").matches());
        assertFalse(reg.matcher("aaaaa").matches());
        assertFalse(reg.matcher("bbb").matches());
    }

    @Test
    public void operatrions_repeatNoRightArg() {
        RegEx reg = RegEx.compile("a{2,}");

        assertFalse(reg.matcher("").matches());
        assertFalse(reg.matcher("a").matches());
        assertTrue(reg.matcher("aa").matches());
        assertTrue(reg.matcher("aaa").matches());
        assertTrue(reg.matcher("aaaa").matches());
        assertTrue(reg.matcher("aaaaaaaaaaaaaaaaaaaaa").matches());
        assertFalse(reg.matcher("bbb").matches());
    }

    @Test
    public void operatrions_repeatNoBoth() {
        RegEx reg = RegEx.compile("a{,}");

        assertTrue(reg.matcher("").matches());
        assertTrue(reg.matcher("a").matches());
        assertTrue(reg.matcher("aa").matches());
        assertTrue(reg.matcher("aaa").matches());
        assertTrue(reg.matcher("aaaa").matches());
        assertTrue(reg.matcher("aaaaaaaaaaaaaaaaaaaaa").matches());
        assertFalse(reg.matcher("bbb").matches());
    }

    @Test
    public void operatrions_lookahead() {
        RegEx reg = RegEx.compile("a/b");

        assertFalse(reg.matcher("a").matches());
        assertFalse(reg.matcher("b").matches());
        assertFalse(reg.matcher("c").matches());
        assertTrue(reg.matcher("ab").matches());
        assertFalse(reg.matcher("ba").matches());
        assertFalse(reg.matcher("").matches());
    }

    @Test
    public void operatrionPrioritiesTest1() {
        RegEx reg = RegEx.compile("ab*");

        assertTrue(reg.matcher("abbbbb").matches());
        assertTrue(reg.matcher("a").matches());
        assertFalse(reg.matcher("abab").matches());
        assertFalse(reg.matcher("").matches());
        assertFalse(reg.matcher("aab").matches());
    }

    @Test
    public void operatrionPrioritiesTest2() {
        RegEx reg = RegEx.compile("ab|cd");

        assertTrue(reg.matcher("ab").matches());
        assertTrue(reg.matcher("cd").matches());
        assertFalse(reg.matcher("abd").matches());
        assertFalse(reg.matcher("acd").matches());
    }

    @Test
    public void operatrionPrioritiesTest3() {
        RegEx reg = RegEx.compile("a(b|c)d");

        assertFalse(reg.matcher("ab").matches());
        assertFalse(reg.matcher("cd").matches());
        assertTrue(reg.matcher("abd").matches());
        assertTrue(reg.matcher("acd").matches());
    }
}