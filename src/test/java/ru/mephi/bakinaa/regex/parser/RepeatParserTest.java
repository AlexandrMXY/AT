package ru.mephi.bakinaa.regex.parser;

import org.junit.jupiter.api.Test;
import ru.mephi.bakinaa.regex.tree.EpsChar;
import ru.mephi.bakinaa.regex.tree.raw.Repeat;

import static org.junit.jupiter.api.Assertions.*;

class RepeatParserTest {
    @Test
    public void createRepeat_bothBoundsCorrect() {
        Repeat r = RepeatParser.createRepeat(new EpsChar(), "1,2");
        assertEquals(1, r.getFrom());
        assertEquals(2, r.getTo());
    }

    @Test
    public void createRepeat_bothBoundsCorrect2() {
        Repeat r = RepeatParser.createRepeat(new EpsChar(), "2,2");
        assertEquals(2, r.getFrom());
        assertEquals(2, r.getTo());
    }

    @Test
    public void createRepeat_noLeftBound() {
        Repeat r = RepeatParser.createRepeat(new EpsChar(), ",2");
        assertEquals(0, r.getFrom());
        assertEquals(2, r.getTo());
    }

    @Test
    public void createRepeat_noRightBound() {
        Repeat r = RepeatParser.createRepeat(new EpsChar(), "1,");
        assertEquals(1, r.getFrom());
        assertEquals(Integer.MAX_VALUE, r.getTo());
    }

    @Test
    public void createRepeat_noBounds() {
        Repeat r = RepeatParser.createRepeat(new EpsChar(), ",");
        assertEquals(0, r.getFrom());
        assertEquals(Integer.MAX_VALUE, r.getTo());
    }

    @Test
    public void createRepeat_negativeBound_shouldThrow() {
        assertThrows(ParserException.class, () -> {
            RepeatParser.createRepeat(new EpsChar(), "-1,1");
        });
        assertThrows(ParserException.class, () -> {
            RepeatParser.createRepeat(new EpsChar(), "-1,-1");
        });
    }

    @Test
    public void createRepeat_invalidBoundsOrder_shouldThrow() {
        assertThrows(ParserException.class, () -> {
            RepeatParser.createRepeat(new EpsChar(), "2,1");
        });
    }

    @Test
    public void createRepeat_noSeparator_shouldThrow() {
        assertThrows(ParserException.class, () -> {
            RepeatParser.createRepeat(new EpsChar(), "2");
        });
    }

    @Test
    public void createRepeat_illegalChar_shouldThrow() {
        assertThrows(ParserException.class, () -> {
            RepeatParser.createRepeat(new EpsChar(), "2,.3");
        });
    }
}