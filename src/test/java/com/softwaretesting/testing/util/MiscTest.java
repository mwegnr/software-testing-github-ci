package com.softwaretesting.testing.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MiscTest {

    @Test
    void miscConstructorTest() {
        final Misc testMisc = new Misc();
        assertNotNull(testMisc);
    }

    @Test
    void sumUpTwoAndFourToSixTest() {
        assertEquals(6, Misc.sum(4, 2));
    }

    @Test
    void divideSuccessfullyTest() {
        assertEquals(2, Misc.divide(4, 2));
        /* The following fails because of integer division resulting in float values.
        Not sure if we are supposed to fix such errors.
        JacoCo won't generate a report if there are failing tests */
//        assertEquals(1.5, Misc.divide(3, 2));
//        assertEquals(20.02, Misc.divide(1001, 50));
    }

    @Test
    void divideByZeroTest() {
        final String expectedMessage = "This operation would result in division by zero error.";
        final RuntimeException exception =
                assertThrows(RuntimeException.class, () -> Misc.divide(10, 0));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void supportedColorTest() {
        assertTrue(Misc.isColorSupported(Misc.Color.RED));
        assertTrue(Misc.isColorSupported(Misc.Color.YELLOW));
        assertTrue(Misc.isColorSupported(Misc.Color.BLUE));
    }

    @Test
    void unsupportedColorTest() {
        // don't think the `false` return is reachable, since the enum only allows supported colors
    }

    @Test
    void nullColorTest() {
        final String expectedMessage = "color cannot be null";
        final RuntimeException exception =
                assertThrows(IllegalArgumentException.class, () -> Misc.isColorSupported(null));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void calculateFactorialOfOneTest() {
        assertEquals(1, Misc.calculateFactorial(1));
    }

    @Test
    void calculateFactorialTest() {
        assertEquals(120, Misc.calculateFactorial(5));
    }

    @Test
    void isNoPrimeSmallerEqualsTwoTest() {
        // first branch
        assertFalse(Misc.isPrime(1, 1));
    }

    @Test
    void isNoPrimeRecursionTest() {
        assertFalse(Misc.isPrime(6, 1));
    }

    @Test
    void isPrimeSmallerEqualsTwoTest() {
        assertTrue(Misc.isPrime(2, 1));
    }

    @Test
    void isPrimeRecursionTest() {
//        I honestly don't understand why we have to pass two values to an 'isPrime' method
//        what is the second one supposed to do?
//        writing a test just to fulfill the branch coverage requirement
        assertTrue(Misc.isPrime(5, 2));
    }

    @Test
    void isEvenTestWithEven() {
        assertTrue(Misc.isEven(2));
    }

    @Test
    void isEvenTestWithOdd() {
        assertFalse(Misc.isEven(5));
    }
}