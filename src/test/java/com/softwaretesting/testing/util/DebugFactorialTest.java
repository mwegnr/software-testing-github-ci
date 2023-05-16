package com.softwaretesting.testing.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DebugFactorialTest {

    @Test
    void instantiationWithExceptionTest() throws NoSuchMethodException {
        final String expectedMessage = "Instantiation of utility class is not allowed";

        // prep instantiation via reflection
        final Constructor<DebugFactorial> debugFactorialReflectionConstructor
                = DebugFactorial.class.getDeclaredConstructor();
        debugFactorialReflectionConstructor.setAccessible(true);

        // trying this should cause an InvocationTargetException
        // wrapping the UnsupportedOperationException of the DebugFactorial constructor
        final InvocationTargetException outerException = assertThrows(InvocationTargetException.class, debugFactorialReflectionConstructor::newInstance);

        assertEquals(UnsupportedOperationException.class, outerException.getTargetException().getClass());
        assertEquals(expectedMessage, outerException.getTargetException().getMessage());
    }

    @ParameterizedTest
    @CsvSource({"1,1", "2,2", "3,6"})
    void factorialTest(int input, int expectedOutput) {
        assertEquals(expectedOutput, DebugFactorial.factorial(input));
    }
}