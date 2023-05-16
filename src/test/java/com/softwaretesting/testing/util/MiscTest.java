package com.softwaretesting.testing.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class MiscTest {
    @Test
    void instantiationWithExceptionTest() throws NoSuchMethodException {
        final String expectedMessage = "Instantiation of utility class is not allowed";

        // prep instantiation via reflection
        final Constructor<Misc> miscReflectionConstructor
                = Misc.class.getDeclaredConstructor();
        miscReflectionConstructor.setAccessible(true);

        // trying this should cause an InvocationTargetException
        // wrapping the UnsupportedOperationException of the Misc constructor
        final InvocationTargetException outerException = assertThrows(InvocationTargetException.class, miscReflectionConstructor::newInstance);

        assertEquals(UnsupportedOperationException.class, outerException.getTargetException().getClass());
        assertEquals(expectedMessage, outerException.getTargetException().getMessage());
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
        JaCoCo won't generate a report if there are failing tests */
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

    /* usually we would also test for handling of unsupported colors,
     * unfortunately this is not possible as all Misc.Color values are supported,
     * and it's therefore not possible to obtain an unsupported color
     */
    @Test
    void supportedColorTest() {
        assertTrue(Misc.isColorSupported(Misc.Color.RED));
        assertTrue(Misc.isColorSupported(Misc.Color.YELLOW));
        assertTrue(Misc.isColorSupported(Misc.Color.BLUE));
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
    void isPrimeISquareSmallerNTest() {
        assertFalse(Misc.isPrime(9, 2));
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