package ru.mephi.bakinaa.regex.chars;

import org.junit.jupiter.api.Test;

import javax.swing.plaf.PanelUI;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharGroupTest {
    @Test
    public void splitIntersect_noIntersect_throw() {
        assertThrows(IllegalArgumentException.class, () -> new CharGroup('a', 'b').splitIntersect(new CharGroup('c', 'd')));
    }

    @Test
    public void splitIntersect_inside_returnCorrect() {
        List<CharGroup> charGroups = new CharGroup('a', 'z').splitIntersect(new CharGroup('f', 'h'));
        assertIterableEquals(List.of(
                new CharGroup('a', 'e'),
                new CharGroup('f', 'h'),
                new CharGroup('i', 'z')
        ), charGroups);
    }

    @Test
    public void splitIntersect_sameRightBorder_returnCorrect() {
        List<CharGroup> charGroups = new CharGroup('a', 'h').splitIntersect(new CharGroup('f', 'h'));
        assertIterableEquals(List.of(
                new CharGroup('a', 'e'),
                new CharGroup('f', 'h')
        ), charGroups);
    }


    @Test
    public void splitIntersect_sameLeftBorder_returnCorrect() {
        List<CharGroup> charGroups = new CharGroup('a', 'z').splitIntersect(new CharGroup('a', 'h'));
        assertIterableEquals(List.of(
                new CharGroup('a', 'h'),
                new CharGroup('i', 'z')
        ), charGroups);
    }


    @Test
    public void splitIntersect_1letter_returnCorrect() {
        List<CharGroup> charGroups = new CharGroup('a', 'h').splitIntersect(new CharGroup('h', 'h'));
        assertIterableEquals(List.of(
                new CharGroup('a', 'g'),
                new CharGroup('h', 'h')
        ), charGroups);
    }


    @Test
    public void splitIntersect_intersect_returnCorrect() {
        List<CharGroup> charGroups = new CharGroup('a', 'h').splitIntersect(new CharGroup('f', 'z'));
        assertIterableEquals(List.of(
                new CharGroup('a', 'e'),
                new CharGroup('f', 'h'),
                new CharGroup('i', 'z')
        ), charGroups);
    }

}