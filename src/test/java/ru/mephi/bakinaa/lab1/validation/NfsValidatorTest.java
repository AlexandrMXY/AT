package ru.mephi.bakinaa.lab1.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.mephi.bakinaa.lab1.validation.flex.FlexValidator;
import ru.mephi.bakinaa.lab1.validation.smc.SmcValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class NfsValidatorTest {
    private static Stream<Arguments> validators() {
        return Stream.of(
                Arguments.of(new RegExValidator()),
                Arguments.of(new SmcValidator()),
                Arguments.of(new FlexValidator()));
    }

    @ParameterizedTest
    @MethodSource("validators")
    public void null_invalid(NfsValidator validator) {
        assertFalse(validator.isValid(null));
    }

    @ParameterizedTest
    @MethodSource("validators")
    public void empty_invalid(NfsValidator validator) {
        assertFalse(validator.isValid(""));
    }

    @ParameterizedTest
    @MethodSource("validators")
    public void noHeader_invalid(NfsValidator validator) {
        assertFalse(validator.isValid("qwerty/qwerty/qwerty"));
        assertFalse(validator.isValid("/qwerty/qwerty/qwerty"));
    }

    @ParameterizedTest
    @MethodSource("validators")
    public void invalidCharacter_invalid(NfsValidator validator) {
        assertFalse(validator.isValid("nfs://qwrty/qwert5"));
        assertFalse(validator.isValid("nfs://qw5rty/qwert"));
        assertFalse(validator.isValid("5nfs://qwrty/qwert"));
    }

    @ParameterizedTest
    @MethodSource("validators")
    public void noServer_invalid(NfsValidator validator) {
        assertFalse(validator.isValid("nfs://"));
    }

    @ParameterizedTest
    @MethodSource("validators")
    public void noCatalog_invalid(NfsValidator validator) {
        assertFalse(validator.isValid("nfs://qwerty"));
    }

    @ParameterizedTest
    @MethodSource("validators")
    public void invalidSeparators_invalid(NfsValidator validator) {
        assertFalse(validator.isValid("nfs:///qwerty/qwerty"));
        assertFalse(validator.isValid("nfs://qwerty/qwerty/"));
        assertFalse(validator.isValid("nfs://qwerty//qwerty"));
    }

    @ParameterizedTest
    @MethodSource("validators")
    public void tooLong_invalid(NfsValidator validator) {
        assertFalse(validator.isValid("nfs://qwerty/qwerty/qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"));
    }

    @ParameterizedTest
    @MethodSource("validators")
    public void valid(NfsValidator validator) {
        assertTrue(validator.isValid("nfs://qwerty/qwerty/qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"));
    }
}