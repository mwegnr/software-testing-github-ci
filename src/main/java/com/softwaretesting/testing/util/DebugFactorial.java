package com.softwaretesting.testing.util;

public final class DebugFactorial {
// Java program to find factorial of given number

    private DebugFactorial() {
        throw new UnsupportedOperationException("Instantiation of utility class is not allowed");
    }

    // Method to find factorial of given number
    static int factorial(int n) {
        int res = 1;
        for (int i = 2; i <= n; i++)
            res *= i;
        return res;
    }
}
