package com.softwaretesting.testing.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PhoneNumberValidatorTest {
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
    @Disabled
    public void validNumberMinLengthTest() {
        assertTrue(phoneNumberValidator.validate("+4912345"));
    }

    @Test
    @Disabled
    public void validNumberMaxLengthTest() {
        assertTrue(phoneNumberValidator.validate("+491234567890123"));
    }

    // invalid test cases
    @Test
    public void invalidNumberEmpty() {
        assertFalse(phoneNumberValidator.validate(""));
    }

    @Test
    @Disabled
    public void invalidNumberCountryCodeStartingWithZeroTest() {
        assertFalse(phoneNumberValidator.validate("+049123456789"));
    }

    @Test
    @Disabled
    public void invalidNumberTooShort() {
        assertFalse(phoneNumberValidator.validate("+49170"));
    }

    @Test
    @Disabled
    public void invalidNumberTooLong() {
        assertFalse(phoneNumberValidator.validate("+4917012345678901"));
    }

    @Test
    @Disabled
    public void invalidNumberIllegalSymbols() {
        assertFalse(phoneNumberValidator.validate("+49123AB456789"));
    }

}