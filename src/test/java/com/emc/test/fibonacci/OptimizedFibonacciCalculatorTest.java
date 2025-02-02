package com.emc.test.fibonacci;

import org.junit.Before;
import org.junit.Test;
import java.math.BigInteger;
import static org.junit.Assert.*;

public class OptimizedFibonacciCalculatorTest {
    
    private OptimizedFibonacciCalculator calculator;
    
    @Before
    public void setup() {
        calculator = new OptimizedFibonacciCalculator();
    }
    
    @Test
    public void testBasicFibonacciNumbers() {
        assertEquals(BigInteger.ZERO, calculator.calculate(0));
        assertEquals(BigInteger.ONE, calculator.calculate(1));
        assertEquals(BigInteger.ONE, calculator.calculate(2));
        assertEquals(new BigInteger("2"), calculator.calculate(3));
        assertEquals(new BigInteger("3"), calculator.calculate(4));
        assertEquals(new BigInteger("5"), calculator.calculate(5));
    }
    
    @Test
    public void testLargerFibonacciNumber() {
        assertEquals(new BigInteger("55"), calculator.calculate(10));
        assertEquals(new BigInteger("6765"), calculator.calculate(20));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInput() {
        calculator.calculate(-1);
    }
    
    @Test
    public void testCaching() {
        BigInteger first = calculator.calculate(15);
        BigInteger second = calculator.calculate(15);
        assertEquals(first, second);
    }
    
    @Test
    public void testClearCache() {
        calculator.calculate(15);
        calculator.clearCache();
        calculator.calculate(15);
    }
    
    @Test
    public void testLargeNumber() {
        BigInteger result = calculator.calculate(100);
        assertTrue(result.compareTo(BigInteger.ZERO) > 0);
    }
}
