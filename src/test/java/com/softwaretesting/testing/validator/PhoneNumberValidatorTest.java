package com.softwaretesting.testing.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberValidatorTest {
    /*
     * Phone Number Constraints according to E.164
     *   - Country Code: "+" + 1-3 digits, cannot start with 0
     *       - length cannot be tested in practice due to no clear separator between country code and main part
     *   - Maximum length (incl country code): 15 digits
     *   - Minimal length (incl country code): not specified in E.164, but in practice 7 digits (e.g. Niue)
     *       - see: https://stackoverflow.com/a/17814276
     *  */

    private PhoneNumberValidator phoneNumberValidator;

    @BeforeEach
    public void initFreshValidator() {
        phoneNumberValidator = new PhoneNumberValidator();
    }

    // valid test cases
    @Test
    void validNumberMinLengthTest() {
        assertTrue(phoneNumberValidator.validate("+4912345"));
    }

    @Test
    void validNumberMaxLengthTest() {
        assertTrue(phoneNumberValidator.validate("+491234567890123"));
    }

    // invalid test cases
    @Test
    void invalidNumberNull() {
        final String expectedMessage = "Number must not be null";

        final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> phoneNumberValidator.validate(null));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void invalidNumberEmpty() {
        assertFalse(phoneNumberValidator.validate(""));
    }

    @Test
    void invalidNumberCountryCodeStartingWithZeroTest() {
        assertFalse(phoneNumberValidator.validate("+049123456789"));
    }

    @Test
    void invalidNumberTooShort() {
        assertFalse(phoneNumberValidator.validate("+49170"));
    }

    @Test
    void invalidNumberTooLong() {
        assertFalse(phoneNumberValidator.validate("+4917012345678901"));
    }

    @Test
    void invalidNumberIllegalSymbols() {
        assertFalse(phoneNumberValidator.validate("+49123AB456789"));
    }
}