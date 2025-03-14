package ru.mephi.bakinaa.regex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdvancedRegExElementsTest {
    @Test
    public void captures_valid1CaptureGroup_returnGroupContent() {
        RegEx regex = RegEx.compile("#([a-z]*)");
        var matcher = regex.matcher("qwerty");
        assertTrue(matcher.matches());
        assertEquals("qwerty", matcher.getCaptures().get(0));
    }

    @Test
    public void captures_valid2CaptureGroup_returnGroupContent1() {
        RegEx regex = RegEx.compile("#([a-z]*)A#([a-z]*)");
        var matcher = regex.matcher("qwertyAfggh");
        assertTrue(matcher.matches());
        assertEquals("qwerty", matcher.getCaptures().get(0));
        assertEquals("fggh", matcher.getCaptures().get(1));
    }

    @Test
    public void captures_valid1CaptureGroup_returnGroupContent2() {
        RegEx regex = RegEx.compile("#([a-z]*)A#([a-z]*)");
        var matcher = regex.matcher("qwertyA");
        assertTrue(matcher.matches());
        assertEquals("qwerty", matcher.getCaptures().get(0));
        assertEquals("", matcher.getCaptures().get(1));
    }

    @Test
    public void captures_noCaptureGroups_shouldThrow() {
        RegEx regex = RegEx.compile("");
        var matcher = regex.matcher("");
        assertTrue(matcher.matches());
        assertThrows(IllegalStateException.class, () -> matcher.getCaptures().get(0));
    }

    @Test
    public void captures_invalidGroupId_shouldThrow() {
        RegEx regex = RegEx.compile("#([a-z]*)");
        var matcher = regex.matcher("a");
        assertTrue(matcher.matches());
        assertThrows(IllegalStateException.class, () -> matcher.getCaptures().get(1));
    }

    @Test
    public void captures_negativeGroupId_shouldThrow() {
        RegEx regex = RegEx.compile("#([a-z]*)");
        var matcher = regex.matcher("a");
        assertTrue(matcher.matches());
        assertThrows(IllegalStateException.class, () -> matcher.getCaptures().get(-1));
    }

    @Test
    public void captures_notMatched_shouldThrow() {
        RegEx regex = RegEx.compile("#([a-z]*)");
        var matcher = regex.matcher("a");
        assertThrows(IllegalStateException.class, () -> matcher.getCaptures().get(0));
    }

    @Test
    public void captures_notMatches_shouldThrow() {
        RegEx regex = RegEx.compile("#([a-z]*)");
        var matcher = regex.matcher("A");
        assertFalse(matcher.matches());
        assertThrows(IllegalStateException.class, () -> matcher.getCaptures().get(0));
    }

    @Test
    public void captures_repeatOnCaptureGroup_shouldThorw() {
        assertThrows(RegExException.class, () ->
                RegEx.compile("#(a){1,2}"));
    }

    @Test
    public void captures_plusOnCaptureGroup_shouldThorw() {
        assertThrows(RegExException.class, () ->
                RegEx.compile("#(a)+"));
    }

    @Test
    public void captures_starCaptureGroup_shouldThorw() {
        assertThrows(RegExException.class, () ->
                RegEx.compile("#(a)*"));
    }

    @Test
    public void backreference_validBackreference_shouldMatchesCorrectly() {
        RegEx regex = RegEx.compile("#([a-z]*)f#0");
        assertTrue(regex.matcher("afa").matches());
        assertTrue(regex.matcher("aafaa").matches());
        assertTrue(regex.matcher("f").matches());
        assertFalse(regex.matcher("aafa").matches());
        assertFalse(regex.matcher("afaa").matches());
        assertFalse(regex.matcher("aa").matches());
    }

    @Test
    public void backreference_validBackreference_shouldMatchesCorrectly_2() {
        RegEx regex = RegEx.compile("#([a-z]*)#0");
        assertTrue(regex.matcher("aa").matches());
        assertTrue(regex.matcher("aaaa").matches());
        assertTrue(regex.matcher("").matches());
        assertFalse(regex.matcher("aaa").matches());
        assertFalse(regex.matcher("a").matches());
        assertFalse(regex.matcher("aaaab").matches());
    }

    @Test
    public void backreference_validBackreference_shouldCaptureCorrectValue() {
        RegEx regex = RegEx.compile("#([a-z]*)#0");

        var matcher = regex.matcher("avavavavavav");
        assertTrue(matcher.matches());
        assertEquals("avavav", matcher.getCaptures().get(0));
    }

    @Test
    public void backreference_backreferenceBeforCaptureGroup_shouldThrow() {
        assertThrows(RegExException.class, () -> {RegEx.compile("#0 #(a*)");});
    }

    @Test
    public void backreference_backreferenceBeforCaptureGroup2_shouldThrow() {
        assertThrows(RegExException.class, () -> {RegEx.compile("#(a*) #1 #(a*)");});
    }

    @Test
    public void backreference_backreferenceWithInvalidId_shouldThrow() {
        assertThrows(RegExException.class, () -> {RegEx.compile("#(a*) #10");});
    }

    @Test
    public void backreference_backreferenceInsideGroup_shouldThrow() {
        assertThrows(RegExException.class, () -> {RegEx.compile("#(a*#0)");});
    }

    @Test
    public void backreference_backreferenceInsideAnotherGroup_shouldMatchCorrectly() {
        var regex = RegEx.compile("#(a*) #(f#0)");
        var matcher = regex.matcher("aaaaa faaaaa");
        assertTrue(matcher.matches());
        assertEquals("aaaaa", matcher.getCaptures().get(0));
        assertEquals("faaaaa", matcher.getCaptures().get(1));
    }

    @Test
    public void lookahead_validUsage_shouldMatchCorrectly() {
        var regex = RegEx.compile("a{2,}/b{2,}");
        assertTrue(regex.matcher("aaaaabbb").matches());
        assertTrue(regex.matcher("aabb").matches());
        assertFalse(regex.matcher("aaabbbf").matches());
        assertFalse(regex.matcher("aaab").matches());
        assertFalse(regex.matcher("abbb").matches());
        assertFalse(regex.matcher("aaa/bbb").matches());
    }

    @Test
    public void lookahead_usageNotAsRoot_shouldThrow() {
        assertThrows(RegExException.class, () -> { RegEx.compile("a(b/c)"); });
    }
}
