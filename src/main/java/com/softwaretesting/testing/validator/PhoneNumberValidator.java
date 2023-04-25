package com.softwaretesting.testing.validator;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class PhoneNumberValidator {
    public boolean validate(final String phoneNumber) {
        if (phoneNumber == null) {
            throw new IllegalArgumentException("Number must not be null");
        }

        final Pattern validPhoneNumberRegex = Pattern.compile("^\\+[1-9][0-9]{6,14}$");
        return validPhoneNumberRegex.matcher(phoneNumber).find();
    }
}
